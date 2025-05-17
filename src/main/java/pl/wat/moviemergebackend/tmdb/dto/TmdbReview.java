package pl.wat.moviemergebackend.tmdb.dto;

import pl.wat.moviemergebackend.movie.dto.Review;

import java.util.List;

public record TmdbReview(
        int totalPages,
        int totalResults,
        int page,
        List<Review> reviews
) {
}
