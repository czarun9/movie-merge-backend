package pl.wat.moviemergebackend.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.movie.dto.UserMovieListItemRequest;
import pl.wat.moviemergebackend.movie.dto.CreateListRequest;
import pl.wat.moviemergebackend.movie.dto.GetListResponse;
import pl.wat.moviemergebackend.movie.service.UserListsService;
import pl.wat.moviemergebackend.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/lists")
@RequiredArgsConstructor
public class UserListsController {

    private final UserListsService userListsService;

    @GetMapping
    public ResponseEntity<List<GetListResponse>> getUserLists(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<GetListResponse> lists = userListsService.getUserLists(principal.getId());
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<GetListResponse> getUserList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId
    ) {
        GetListResponse list = userListsService.getUserList(listId, principal.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<GetListResponse> createList(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateListRequest request
    ) {
        GetListResponse createdList;
        if (request.initialMovieId() != null) {
            createdList = userListsService.createListWithMovie(principal.getId(), request);
        } else {
            createdList = userListsService.createList(principal.getId(), request.name());
        }
        return ResponseEntity.ok(createdList);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId
    ) {
        userListsService.deleteList(principal.getId(), listId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{listId}/item")
    public ResponseEntity<Void> addMovieToList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @RequestBody UserMovieListItemRequest request
    ) {
        userListsService.addMovieToList(principal.getId(), listId, request.movieTmdbId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listId}/item/{movieTmdbId}")
    public ResponseEntity<Void> removeMovieFromList(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @PathVariable Integer movieTmdbId
    ) {
        userListsService.removeMovieFromList(principal.getId(), listId, movieTmdbId);
        return ResponseEntity.noContent().build();
    }


}
