package pl.wat.moviemergebackend.tmdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omertron.themoviedbapi.MovieDbException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;
import pl.wat.moviemergebackend.tmdb.service.TmdbService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    @GetMapping("/")
    public String getTmdbMovie(){
        try {
            return tmdbService.getTmdbMovie();
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/discover")
    public ResponseEntity<List<TmdbMovie>> getDiscoverTmdbMovies() {
        try {
            List<TmdbMovie> movies = tmdbService.getDiscoverTmdbMovies();
            return ResponseEntity.ok(movies);
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
