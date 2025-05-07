package pl.wat.moviemergebackend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.api.dto.ChangeStatusDto;
import pl.wat.moviemergebackend.api.dto.RatingDto;
import pl.wat.moviemergebackend.security.UserPrincipal;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto;
import pl.wat.moviemergebackend.service.MovieStatusService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieStatusController {

    private final MovieStatusService movieStatusService;

    @GetMapping("/{tmdbId}/status")
    public ResponseEntity<MovieStatusDto> getMovieStatus(
            @PathVariable Integer tmdbId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        MovieStatusDto status = movieStatusService.getMovieStatus(userId, tmdbId);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/{tmdbId}/favourite")
    public ResponseEntity<Void> setFavouriteStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatusDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.isStatus(), movieStatusService::setFavoriteStatus);
    }

    @PatchMapping("/{tmdbId}/watched")
    public ResponseEntity<Void> setWatchedStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatusDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.isStatus(), movieStatusService::setWatchedStatus);
    }

    @PatchMapping("/{tmdbId}/watchlist")
    public ResponseEntity<Void> setAddedToWatchlistStatus(
            @PathVariable Integer tmdbId,
            @RequestBody ChangeStatusDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return handleStatusChange(principal.getId(), tmdbId, request.isStatus(), movieStatusService::setAddedToWatchlistStatus);
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
            @RequestBody RatingDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        movieStatusService.setRating(userId, tmdbId, request.getValue());
        return ResponseEntity.ok().build();
    }

}
