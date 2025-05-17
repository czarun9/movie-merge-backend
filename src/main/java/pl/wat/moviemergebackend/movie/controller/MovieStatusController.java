package pl.wat.moviemergebackend.movie.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.movie.dto.ChangeStatus;
import pl.wat.moviemergebackend.movie.dto.Rating;
import pl.wat.moviemergebackend.security.UserPrincipal;
import pl.wat.moviemergebackend.movie.dto.MovieStatus;
import pl.wat.moviemergebackend.movie.service.MovieStatusService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieStatusController {

    private final MovieStatusService movieStatusService;

    @GetMapping("/{tmdbId}/status")
    public ResponseEntity<MovieStatus> getMovieStatus(
            @PathVariable Integer tmdbId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        MovieStatus status = movieStatusService.getMovieStatus(userId, tmdbId);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/{tmdbId}/favourite")
    public ResponseEntity<Void> setFavouriteStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatus request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.status(), movieStatusService::setFavoriteStatus);
    }

    @PatchMapping("/{tmdbId}/watched")
    public ResponseEntity<Void> setWatchedStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatus request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.status(), movieStatusService::setWatchedStatus);
    }

    @PatchMapping("/{tmdbId}/watchlist")
    public ResponseEntity<Void> setAddedToWatchlistStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatus request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.status(), movieStatusService::setAddedToWatchlistStatus);
    }

    private ResponseEntity<Void> handleStatusChange(
            UUID userId,
            Integer tmdbId,
            boolean status,
            TriConsumer<UUID, Integer, Boolean> statusSetter
    ) {
        statusSetter.accept(userId, tmdbId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tmdbId}/rating")
    public ResponseEntity<Void> setRating(
            @PathVariable Integer tmdbId,
            @RequestBody Rating request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        movieStatusService.setRating(userId, tmdbId, request.value());
        return ResponseEntity.ok().build();
    }

}
