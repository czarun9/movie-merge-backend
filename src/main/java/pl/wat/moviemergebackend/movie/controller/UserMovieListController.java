package pl.wat.moviemergebackend.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.movie.dto.UserMovieListItemRequest;
import pl.wat.moviemergebackend.movie.dto.UserMovieListRequest;
import pl.wat.moviemergebackend.movie.dto.UserMovieListResponse;
import pl.wat.moviemergebackend.movie.service.UserMovieListService;
import pl.wat.moviemergebackend.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/lists")
@RequiredArgsConstructor
public class UserMovieListController {

    private final UserMovieListService userMovieListService;

    @GetMapping
    public ResponseEntity<List<UserMovieListResponse>> getUserLists(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<UserMovieListResponse> lists = userMovieListService.getUserLists(principal.getId());
        return ResponseEntity.ok(lists);
    }


    @PostMapping
    public ResponseEntity<UserMovieListResponse> createList(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserMovieListRequest request
    ) {
        UserMovieListResponse createdList = userMovieListService.createList(principal.getId(), request);
        return ResponseEntity.ok(createdList);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId
    ) {
        userMovieListService.deleteList(principal.getId(), listId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{listId}/item")
    public ResponseEntity<Void> addMovieToList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @RequestBody UserMovieListItemRequest request
    ) {
        userMovieListService.addMovieToList(principal.getId(), listId, request.movieTmdbId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listId}/item/{movieTmdbId}")
    public ResponseEntity<Void> removeMovieFromList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @PathVariable Integer movieTmdbId
    ) {
        userMovieListService.removeMovieFromList(principal.getId(), listId, movieTmdbId);
        return ResponseEntity.noContent().build();
    }



}
