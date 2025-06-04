package pl.wat.moviemergebackend.trakt.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.trakt.model.TraktMovie;
import pl.wat.moviemergebackend.trakt.service.TraktService;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/trakt")
public class TraktController {

    private final TraktService traktService;

    @GetMapping("/movies/{tmdbId}")
    public ResponseEntity<TraktMovie> getTraktMovie(@PathVariable int tmdbId) {
        log.info("Pobieranie danych filmu z Trakt dla TMDB ID: {}", tmdbId);

        try {
            Optional<TraktMovie> movieOptional = traktService.getTraktMovieByTmdbId(tmdbId);

            return movieOptional
                    .map(movie -> {
                        log.info("Znaleziono film w Trakt dla TMDB ID: {}", tmdbId);
                        return ResponseEntity.ok(movie);
                    })
                    .orElseGet(() -> {
                        log.info("Nie znaleziono filmu w Trakt dla TMDB ID: {}", tmdbId);
                        return ResponseEntity.notFound().build();
                    });

        } catch (TraktService.TraktServiceException e) {
            log.error("Błąd serwisu Trakt dla TMDB ID {}: {}", tmdbId, e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (Exception e) {
            log.error("Nieoczekiwany błąd podczas pobierania filmu z Trakt dla TMDB ID {}: {}", tmdbId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}