package pl.wat.moviemergebackend.trakt.configuration;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Getter
@Configuration
public class TraktApiConfig {

    private static final String TOKEN_PROPERTIES_FILE = "trakt-tokens.properties";
    private static final String ACCESS_TOKEN_KEY = "trakt.access.token";
    private static final String REFRESH_TOKEN_KEY = "trakt.refresh.token";

    @Value("${trakt.api.client_id}")
    private String clientId;

    @Value("${trakt.api.client_secret}")
    private String clientSecret;

    @Value("${trakt.api.redirect_uri:urn:ietf:wg:oauth:2.0:oob}")
    private String redirectUri;

    @Bean
    public TraktV2 traktApi() {
        TraktV2 traktApi = new TraktV2(clientId, clientSecret, redirectUri);

        String accessToken = getSavedAccessToken();
        String refreshToken = getSavedRefreshToken();

        if (accessToken != null && !accessToken.isEmpty()) {
            log.info("Znaleziono zapisany token dostępu, próba użycia...");
            traktApi.accessToken(accessToken);

            if (refreshToken != null && !refreshToken.isEmpty()) {
                traktApi.refreshToken(refreshToken);
            }

            if (isTokenValid(traktApi)) {
                log.info("Zapisany token jest ważny");
                return traktApi;
            } else if (refreshToken != null && !refreshToken.isEmpty()) {
                log.info("Token nieważny, próba odświeżenia...");
                try {
                    return refreshAccessToken(traktApi);
                } catch (Exception e) {
                    log.warn("Nie udało się odświeżyć tokena, rozpoczynam autoryzację od nowa");
                }
            }
        }

        try {
            return authorizeDevice(traktApi);
        } catch (Exception e) {
            log.error("Błąd podczas autoryzacji urządzenia: {}", e.getMessage(), e);
            throw new RuntimeException("Nie można autoryzować urządzenia Trakt", e);
        }
    }

    /**
     * Inicjuje proces autoryzacji urządzenia w Trakt API.
     * Metoda wygeneruje kod urządzenia, który użytkownik musi wprowadzić na stronie Trakt.
     * Po autoryzacji przez użytkownika, metoda uzyska token dostępu.
     */
    private TraktV2 authorizeDevice(TraktV2 traktApi) throws IOException, InterruptedException {
        log.info("Rozpoczynam proces autoryzacji urządzenia...");
        Response<DeviceCode> response = traktApi.generateDeviceCode();

        if (!response.isSuccessful() || response.body() == null) {
            TraktError error = traktApi.checkForTraktError(response);
            throw new IOException("Nie można wygenerować kodu urządzenia: " +
                    (error != null ? error.message : "Unknown error"));
        }

        DeviceCode deviceCode = response.body();
        log.info("========== INSTRUKCJA AUTORYZACJI TRAKT ==========");
        log.info("1. Odwiedź: {} w przeglądarce", deviceCode.verification_url);
        log.info("2. Wprowadź kod: {}", deviceCode.user_code);
        log.info("3. Autoryzuj aplikację");
        log.info("=================================================");
        log.info("Oczekiwanie na autoryzację użytkownika...");

        int interval = deviceCode.interval;
        int expiresIn = deviceCode.expires_in;
        String code = deviceCode.device_code;

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (expiresIn * 1000);

        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(interval * 1000);

            try {
                Response<AccessToken> tokenResponse = traktApi.exchangeDeviceCodeForAccessToken(code);

                if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
                    AccessToken token = tokenResponse.body();

                    saveTokens(token.access_token, token.refresh_token);

                    traktApi.accessToken(token.access_token);
                    traktApi.refreshToken(token.refresh_token);

                    log.info("Autoryzacja urządzenia zakończona pomyślnie");
                    return traktApi;
                } else {
                    if (tokenResponse.code() != 400) {
                        TraktOAuthError error = traktApi.checkForTraktOAuthError(tokenResponse);
                        log.debug("Oczekiwanie na autoryzację: {}",
                                (error != null ? error.error : "Pending authorization"));
                    }
                }
            } catch (IOException e) {
                log.debug("Oczekiwanie na autoryzację przez użytkownika...");
            }
        }

        throw new IOException("Upłynął limit czasu na autoryzację urządzenia");
    }

    private void saveTokens(String accessToken, String refreshToken) {
        Properties properties = new Properties();
        properties.setProperty(ACCESS_TOKEN_KEY, accessToken);
        properties.setProperty(REFRESH_TOKEN_KEY, refreshToken);

        try (FileOutputStream output = new FileOutputStream(TOKEN_PROPERTIES_FILE)) {
            properties.store(output, "Trakt API OAuth Tokens");
            log.info("Tokeny zostały zapisane do pliku {}", TOKEN_PROPERTIES_FILE);
        } catch (IOException e) {
            log.error("Błąd podczas zapisywania tokenów do pliku: {}", e.getMessage(), e);
        }
    }

    private String getSavedAccessToken() {
        File file = new File(TOKEN_PROPERTIES_FILE);
        if (!file.exists()) {
            log.info("Plik z tokenami nie istnieje: {}", TOKEN_PROPERTIES_FILE);
            return null;
        }

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
            String token = properties.getProperty(ACCESS_TOKEN_KEY);
            log.info("Wczytano zapisany token dostępu");
            return token;
        } catch (IOException e) {
            log.error("Błąd podczas odczytu tokenu z pliku: {}", e.getMessage(), e);
            return null;
        }
    }

    private String getSavedRefreshToken() {
        File file = new File(TOKEN_PROPERTIES_FILE);
        if (!file.exists()) {
            return null;
        }

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
            return properties.getProperty(REFRESH_TOKEN_KEY);
        } catch (IOException e) {
            log.error("Błąd podczas odczytu tokenu odświeżania z pliku: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Sprawdza czy aktualny token dostępu jest ważny poprzez
     * wykonanie prostego zapytania do API.
     */
    private boolean isTokenValid(TraktV2 traktApi) {
        try {
            Response<?> response = traktApi.users().profile(UserSlug.ME, null).execute();
            return !TraktV2.isUnauthorized(response) && response.isSuccessful();
        } catch (Exception e) {
            log.warn("Token dostępu wygasł lub jest nieważny: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Odświeża token dostępu używając refresh tokena.
     * Jeśli odświeżenie się nie powiedzie, rozpoczyna proces autoryzacji od nowa.
     */
    public TraktV2 refreshAccessToken(TraktV2 traktApi) {
        try {
            String refreshToken = traktApi.refreshToken();
            if (refreshToken == null || refreshToken.isEmpty()) {
                log.error("Brak tokena odświeżania, wymagana ponowna autoryzacja");
                return authorizeDevice(traktApi);
            }

            log.info("Odświeżanie tokena dostępu...");
            Response<AccessToken> response = traktApi.refreshAccessToken(refreshToken);

            if (response.isSuccessful() && response.body() != null) {
                AccessToken token = response.body();

                saveTokens(token.access_token, token.refresh_token);

                traktApi.accessToken(token.access_token);
                traktApi.refreshToken(token.refresh_token);

                log.info("Token dostępu został pomyślnie odświeżony");
                return traktApi;
            } else {
                TraktOAuthError error = traktApi.checkForTraktOAuthError(response);
                log.error("Nie można odświeżyć tokena dostępu: {}",
                        (error != null ? error.error_description : "Unknown error"));
                return authorizeDevice(traktApi);
            }
        } catch (Exception e) {
            log.error("Błąd podczas odświeżania tokena: {}", e.getMessage(), e);
            try {
                return authorizeDevice(traktApi);
            } catch (Exception ex) {
                throw new RuntimeException("Nie można autoryzować urządzenia po nieudanym odświeżaniu tokena", ex);
            }
        }
    }
}