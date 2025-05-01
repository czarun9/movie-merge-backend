package pl.wat.moviemergebackend.tmdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omertron.themoviedbapi.MovieDbException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wat.moviemergebackend.tmdb.dto.TmdbMoviePageResponse;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;
import pl.wat.moviemergebackend.tmdb.service.TmdbService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    @GetMapping("/{movieId}")
    public ResponseEntity<TmdbMovie> getTmdbMovie(@PathVariable int movieId){
        try {
            TmdbMovie movie = tmdbService.getTmdbMovie(movieId);
            return ResponseEntity.ok(movie);
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/discover")
    public ResponseEntity<TmdbMoviePageResponse> getDiscoverTmdbMovies(int page) {
        try {
            TmdbMoviePageResponse response = tmdbService.getDiscoverTmdbMovies(page);
            return ResponseEntity.ok(response);
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
