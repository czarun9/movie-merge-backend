package pl.wat.moviemergebackend.trakt.service;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Movie;
import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.IdType;
import com.uwetrottmann.trakt5.enums.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.threeten.bp.format.DateTimeFormatter;
import pl.wat.moviemergebackend.trakt.model.TraktComment;
import pl.wat.moviemergebackend.trakt.model.TraktMovie;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TraktService {

    private final TraktV2 traktApi;

    public TraktService(TraktV2 traktApi) {
        this.traktApi = traktApi;
    }

    /**
     * Pobiera dane filmu z Trakt na podstawie TMDB ID.
     * @param tmdbId TMDB ID filmu
     * @return Optional z TraktMovie lub pusty Optional jeśli nie znaleziono
     * @throws TraktServiceException w przypadku błędów komunikacji z API
     */
    public Optional<TraktMovie> getTraktMovieByTmdbId(int tmdbId) {
        try {
            Optional<String> traktIdOpt = findTraktIdByTmdbId(tmdbId);
            if (traktIdOpt.isEmpty()) {
                log.info("Nie znaleziono filmu w Trakt dla TMDB ID: {}", tmdbId);
                return Optional.empty();
            }

            String traktId = traktIdOpt.get();

            Movie movieDetails = getMovieDetails(traktId);

            List<TraktComment> comments = getMovieComments(traktId);

            TraktMovie traktMovie = TraktMovie.from(movieDetails, comments);
            return Optional.of(traktMovie);

        } catch (IOException e) {
            log.error("Błąd komunikacji z Trakt API dla TMDB ID {}: {}", tmdbId, e.getMessage());
            throw new TraktServiceException("Błąd komunikacji z Trakt API", e);
        }
    }

    /**
     * Znajduje Trakt ID na podstawie TMDB ID
     */
    private Optional<String> findTraktIdByTmdbId(int tmdbId) throws IOException {
        Response<List<SearchResult>> searchResponse = traktApi.search()
                .idLookup(IdType.TMDB, String.valueOf(tmdbId), Type.MOVIE, null, null, null)
                .execute();

        if (!searchResponse.isSuccessful()) {
            log.warn("Nieudane wyszukiwanie w Trakt dla TMDB ID {}: kod {}", tmdbId, searchResponse.code());
            return Optional.empty();
        }

        List<SearchResult> results = searchResponse.body();
        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        SearchResult firstResult = results.get(0);
        if (firstResult.movie == null ||
                firstResult.movie.ids == null ||
                firstResult.movie.ids.trakt == null) {
            log.warn("Niepełne dane w wyniku wyszukiwania dla TMDB ID: {}", tmdbId);
            return Optional.empty();
        }

        return Optional.of(String.valueOf(firstResult.movie.ids.trakt));
    }

    /**
     * Pobiera szczegółowe informacje o filmie
     */
    private Movie getMovieDetails(String traktId) throws IOException {
        Response<Movie> movieResponse = traktApi.movies()
                .summary(traktId, Extended.FULL)
                .execute();

        if (!movieResponse.isSuccessful()) {
            throw new TraktServiceException(
                    String.format("Błąd pobierania szczegółów filmu z Trakt (ID: %s): kod %d",
                            traktId, movieResponse.code()));
        }

        Movie movie = movieResponse.body();
        if (movie == null) {
            throw new TraktServiceException("Puste dane filmu dla Trakt ID: " + traktId);
        }

        return movie;
    }

    /**
     * Pobiera komentarze dla filmu (nie rzuca wyjątku jeśli się nie uda)
     */
    private List<TraktComment> getMovieComments(String traktId) {
        try {
            Response<List<Comment>> commentsResponse = traktApi.movies()
                    .comments(traktId, 1, 20, null)
                    .execute();

            if (!commentsResponse.isSuccessful() || commentsResponse.body() == null) {
                log.info("Nie udało się pobrać komentarzy dla filmu {} (kod: {})",
                        traktId, commentsResponse.code());
                return Collections.emptyList();
            }

            return commentsResponse.body().stream()
                    .map(this::mapToTraktComment)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.warn("Błąd podczas pobierania komentarzy dla filmu {}: {}", traktId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Mapuje Comment na TraktComment
     */
    private TraktComment mapToTraktComment(Comment comment) {
        return new TraktComment(
                comment.id,
                comment.user != null ? comment.user.username : "Unknown",
                comment.comment
        );
    }

    /**
     * Wyjątek specyficzny dla TraktService
     */
    public static class TraktServiceException extends RuntimeException {
        public TraktServiceException(String message) {
            super(message);
        }

        public TraktServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}