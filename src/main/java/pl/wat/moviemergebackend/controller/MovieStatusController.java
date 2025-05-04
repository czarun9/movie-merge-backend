package pl.wat.moviemergebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.api.dto.FavouriteDto;
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
            @RequestBody FavouriteDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        movieStatusService.setFavoriteStatus(userId, tmdbId, request.isFavourite());
        return ResponseEntity.ok().build();
    }
}
