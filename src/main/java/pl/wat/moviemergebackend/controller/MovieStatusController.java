package pl.wat.moviemergebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.repository.UserRepository;
import pl.wat.moviemergebackend.security.UserPrincipal;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto.RatingDto;
import pl.wat.moviemergebackend.repository.MovieRatingRepository;
import pl.wat.moviemergebackend.repository.WatchedMovieRepository;
import pl.wat.moviemergebackend.repository.WatchlistEntryRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieStatusController {

    private final WatchedMovieRepository watchedRepo;
    private final WatchlistEntryRepository watchlistRepo;
    private final MovieRatingRepository ratingRepo;

    @GetMapping("/{tmdbId}/status")
    public ResponseEntity<MovieStatusDto> getMovieStatus(
            @PathVariable Integer tmdbId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();

        boolean watched = watchedRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId);
        boolean inWatchlist = watchlistRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId);

        List<MovieStatusDto.RatingDto> ratings = ratingRepo.findByUserIdAndMovieTmdbId(userId, tmdbId)
                .stream()
                .map(r -> {
                    RatingDto dto = new RatingDto();
                    dto.setValue(r.getRatingValue());
                    dto.setRatedAt(r.getRatedAt());
                    return dto;
                })
                .sorted(Comparator.comparing(RatingDto::getRatedAt))
                .collect(Collectors.toList());

        BigDecimal latest = ratings.isEmpty()
                ? null
                : ratings.get(ratings.size() - 1).getValue();

        MovieStatusDto status = new MovieStatusDto();
        status.setWatched(watched);
        status.setInWatchlist(inWatchlist);
        status.setRatings(ratings);
        status.setLatestRating(latest);

        return ResponseEntity.ok(status);
    }
}
