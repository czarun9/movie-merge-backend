package pl.wat.moviemergebackend.tmdb.dto;

import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;

import java.util.List;


public record TmdbMoviesPage(List<TmdbMovie> movies, int totalPages) {
}
