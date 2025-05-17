package pl.wat.moviemergebackend.trakt.controller;


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
import pl.wat.moviemergebackend.trakt.service.TraktService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tmdb")
public class TraktController {

    private final TraktService traktService;


}

