package pl.wat.moviemergebackend.trakt.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.trakt.model.TraktMovie;
import pl.wat.moviemergebackend.trakt.service.TraktService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/trakt")
public class TraktController {

    private final TraktService traktService;

    @GetMapping("/movies/{tmdbId}")
    public ResponseEntity<TraktMovie> getTraktMovie(@PathVariable int tmdbId) {
        try {
            TraktMovie movie = traktService.getTraktMovieByTmdbId(tmdbId);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}