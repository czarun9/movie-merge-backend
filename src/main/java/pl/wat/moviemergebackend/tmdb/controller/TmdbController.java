package pl.wat.moviemergebackend.tmdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omertron.themoviedbapi.MovieDbException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.tmdb.dto.GenresResponse;
import pl.wat.moviemergebackend.tmdb.dto.TmdbMoviePageResponse;
import pl.wat.moviemergebackend.tmdb.model.DiscoverSearchFilters;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;
import pl.wat.moviemergebackend.tmdb.dto.TmdbMovieReviewDto;
import pl.wat.moviemergebackend.tmdb.service.TmdbService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<TmdbMovie> getTmdbMovie(@PathVariable int movieId){
        try {
            TmdbMovie movie = tmdbService.getTmdbMovie(movieId);
            return ResponseEntity.ok(movie);
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/discover")
    public ResponseEntity<TmdbMoviePageResponse> getDiscoverTmdbMovies(DiscoverSearchFilters filters) {
        try {
            TmdbMoviePageResponse response = tmdbService.getDiscoverTmdbMovies(filters);
            return ResponseEntity.ok(response);
        } catch (MovieDbException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/genres")
    public ResponseEntity<GenresResponse> getGenres() {
        try {
            GenresResponse response = tmdbService.getGenres();
            return ResponseEntity.ok(response);
        } catch (MovieDbException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<TmdbMovieReviewDto> getTmdbMovieReviews(@PathVariable int movieId){
        try {
            TmdbMovieReviewDto response = tmdbService.getTmdbMovieReviews(movieId);
            return ResponseEntity.ok(response);
        } catch (MovieDbException e) {
            throw new RuntimeException(e);
        }
    }

}
