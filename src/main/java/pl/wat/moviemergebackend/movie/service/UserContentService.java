package pl.wat.moviemergebackend.movie.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.movie.dto.ListItemDto;
import pl.wat.moviemergebackend.movie.entity.MovieFavouriteStatusEntity;
import pl.wat.moviemergebackend.movie.entity.MovieRatingStatusEntity;
import pl.wat.moviemergebackend.movie.entity.MovieWatchedStatusEntity;
import pl.wat.moviemergebackend.movie.entity.MovieWatchlistStatusEntity;
import pl.wat.moviemergebackend.movie.entity.UserMovieListItemEntity;
import pl.wat.moviemergebackend.movie.repository.*;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;
import pl.wat.moviemergebackend.tmdb.service.TmdbService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserContentService {

    private final FavouriteRepository favouriteRepository;
    private final WatchlistEntryRepository watchlistEntryRepository;
    private final MovieRatingRepository ratingRepository;
    private final WatchedMovieRepository watchedMovieRepository;
    private final ListItemRepository listItemRepository;
    private final TmdbService tmdbService;

    public Page<ListItemDto> getUserFavorites(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                favouriteRepository::findByUserIdOrderByCreatedAtDesc,
                MovieFavouriteStatusEntity::getId,
                MovieFavouriteStatusEntity::getCreatedAt,
                MovieFavouriteStatusEntity::getMovieTmdbId
        );
    }

    public Page<ListItemDto> getUserWatchlist(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                watchlistEntryRepository::findByUserIdOrderByCreatedAtDesc,
                MovieWatchlistStatusEntity::getId,
                MovieWatchlistStatusEntity::getCreatedAt,
                MovieWatchlistStatusEntity::getMovieTmdbId
        );
    }

    public Page<ListItemDto> getUserWatched(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                watchedMovieRepository::findByUserIdOrderByCreatedAtDesc,
                MovieWatchedStatusEntity::getId,
                MovieWatchedStatusEntity::getCreatedAt,
                MovieWatchedStatusEntity::getMovieTmdbId
        );
    }

    public Page<ListItemDto> getUserRatings(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                ratingRepository::findByUserIdOrderByCreatedAtDesc,
                MovieRatingStatusEntity::getId,
                MovieRatingStatusEntity::getCreatedAt,
                MovieRatingStatusEntity::getMovieTmdbId,
                MovieRatingStatusEntity::getRatingValue
        );
    }

    public Page<ListItemDto> getUserMovieListItems(UUID userId, UUID listId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                (uId, pg) -> listItemRepository.findByUser_IdAndList_IdOrderByCreatedAtDesc(uId, listId, pg),
                UserMovieListItemEntity::getId,
                UserMovieListItemEntity::getCreatedAt,
                UserMovieListItemEntity::getMovieTmdbId
        );
    }


    private <T> Page<ListItemDto> getUserList(
            UUID userId,
            Pageable pageable,
            BiFunction<UUID, Pageable, Page<T>> fetchPageFn,
            Function<T, UUID> extractIdFn,
            Function<T, LocalDateTime> extractCreatedAtFn,
            Function<T, Integer> extractTmdbIdFn
            ) {
        return getUserList(userId, pageable, fetchPageFn, extractIdFn, extractCreatedAtFn, extractTmdbIdFn, null);
    }


    private <T> Page<ListItemDto> getUserList(
            UUID userId,
            Pageable pageable,
            BiFunction<UUID, Pageable, Page<T>> fetchPageFn,
            Function<T, UUID> extractIdFn,
            Function<T, LocalDateTime> extractCreatedAtFn,
            Function<T, Integer> extractTmdbIdFn,
            Function<T, BigDecimal> extractRatingValueFn
    ) {
        Page<T> page = fetchPageFn.apply(userId, pageable);

        return page.map(entity -> {
            ListItemDto dto = new ListItemDto();
            dto.setId(extractIdFn.apply(entity));
            dto.setMovieTmdbId(extractTmdbIdFn.apply(entity));
            dto.setCreatedAt(extractCreatedAtFn.apply(entity));

            if (extractRatingValueFn != null) {
                dto.setRating(extractRatingValueFn.apply(entity));
            }

            try {
                TmdbMovie movie = tmdbService.getTmdbMovie(dto.getMovieTmdbId());
                dto.setMovieTmdbId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setPosterUrl(movie.getPosterPath());
                dto.setReleaseDate(movie.getReleaseDate());
            } catch (Exception e) {
//                 log.warn("Nie udało się pobrać filmu TMDB o ID {}", dto.getMovieTmdbId());
            }


            return dto;
        });
    }


    @Transactional
    public void removeItem(UUID userId, String section, UUID id) {
        switch (section.toLowerCase()) {
            case "favorites" -> favouriteRepository.deleteByUserIdAndId(userId, id);
            case "watchlist" -> watchlistEntryRepository.deleteByUserIdAndId(userId, id);
            case "watched" -> watchedMovieRepository.deleteByUserIdAndId(userId, id);
            case "ratings" -> ratingRepository.deleteByUserIdAndId(userId, id);
            default -> throw new IllegalArgumentException("Nieznana sekcja: " + section);
        }
    }
}
