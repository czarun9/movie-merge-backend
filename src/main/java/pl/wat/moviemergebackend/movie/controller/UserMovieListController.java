package pl.wat.moviemergebackend.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.movie.dto.UserMovieListRequest;
import pl.wat.moviemergebackend.movie.dto.UserMovieListResponse;
import pl.wat.moviemergebackend.movie.service.UserMovieListService;
import pl.wat.moviemergebackend.security.UserPrincipal;

@RestController
@RequestMapping("/api/v1/user-lists")
@RequiredArgsConstructor
public class UserMovieListController {

    private final UserMovieListService userMovieListService;

    @PostMapping
    public ResponseEntity<UserMovieListResponse> createList(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserMovieListRequest request
    ) {
        UserMovieListResponse createdList = userMovieListService.createList(principal.getId(), request);
        return ResponseEntity.ok(createdList);
    }
}
