package pl.wat.moviemergebackend.movie.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.movie.dto.ListItemDto;
import pl.wat.moviemergebackend.security.UserPrincipal;
import pl.wat.moviemergebackend.movie.service.UserContentService;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserContentController {

    private final UserContentService userContentService;

    @GetMapping("/favorites")
    public Page<ListItemDto> getFavorites(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = principal.getId();
        return userContentService.getUserFavorites(userId, PageRequest.of(page, size));
    }

    @GetMapping("/watchlist")
    public Page<ListItemDto> getWatchlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = principal.getId();
        return userContentService.getUserWatchlist(userId, PageRequest.of(page, size));
    }

    @GetMapping("/ratings")
    public Page<ListItemDto> getRatings(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = principal.getId();
        return userContentService.getUserRatings(userId, PageRequest.of(page, size));
    }

    @GetMapping("/watched")
    public Page<ListItemDto> getWatched(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = principal.getId();
        return userContentService.getUserWatched(userId, PageRequest.of(page, size));
    }

    @GetMapping("/lists/{listId}/items")
    public ResponseEntity<Page<ListItemDto>> getListItems(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListItemDto> itemsPage = userContentService.getUserMovieListItems(principal.getId(), listId, pageable);
        return ResponseEntity.ok(itemsPage);
    }


    @DeleteMapping("/{section}/{itemId}")
    public void removeItemFromSection(
            @PathVariable String section,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UUID userId = principal.getId();
        userContentService.removeItem(userId, section, itemId);
    }
}
