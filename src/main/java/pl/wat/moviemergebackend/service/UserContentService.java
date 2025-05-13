package pl.wat.moviemergebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.api.dto.ListItemDto;
import pl.wat.moviemergebackend.entity.*;
import pl.wat.moviemergebackend.repository.FavouriteRepository;
import pl.wat.moviemergebackend.repository.MovieRatingRepository;
import pl.wat.moviemergebackend.repository.WatchedMovieRepository;
import pl.wat.moviemergebackend.repository.WatchlistEntryRepository;
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
    private final TmdbService tmdbService;

    public Page<ListItemDto> getUserFavorites(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                favouriteRepository::findByUserId,
                MovieFavouriteStatusEntity::getMovieTmdbId,
                MovieFavouriteStatusEntity::getCreatedAt
        );
    }

    public Page<ListItemDto> getUserWatchlist(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                watchlistEntryRepository::findByUserId,
                MovieWatchlistStatusEntity::getMovieTmdbId,
                MovieWatchlistStatusEntity::getCreatedAt
        );
    }

    public Page<ListItemDto> getUserWatched(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                watchedMovieRepository::findByUserId,
                MovieWatchedStatusEntity::getMovieTmdbId,
                MovieWatchedStatusEntity::getCreatedAt
        );
    }

    public Page<ListItemDto> getUserRatings(UUID userId, Pageable pageable) {
        return getUserList(
                userId,
                pageable,
                ratingRepository::findByUserId,
                MovieRatingStatusEntity::getMovieTmdbId,
                MovieRatingStatusEntity::getCreatedAt,
                MovieRatingStatusEntity::getRatingValue
        );
    }


    private <T> Page<ListItemDto> getUserList(
            UUID userId,
            Pageable pageable,
            BiFunction<UUID, Pageable, Page<T>> fetchPageFn,
            Function<T, Integer> extractTmdbIdFn,
            Function<T, LocalDateTime> extractCreatedAtFn
    ) {
        return getUserList(userId, pageable, fetchPageFn, extractTmdbIdFn, extractCreatedAtFn, null);
    }


    private <T> Page<ListItemDto> getUserList(
            UUID userId,
            Pageable pageable,
            BiFunction<UUID, Pageable, Page<T>> fetchPageFn,
            Function<T, Integer> extractTmdbIdFn,
            Function<T, LocalDateTime> extractCreatedAtFn,
            Function<T, BigDecimal> extractRatingValueFn
    ) {
        Page<T> page = fetchPageFn.apply(userId, pageable);

        return page.map(entity -> {
            ListItemDto dto = new ListItemDto();
            dto.setMovieTmdbId(extractTmdbIdFn.apply(entity));
            dto.setCreatedAt(extractCreatedAtFn.apply(entity));

            if (extractRatingValueFn != null) {
                dto.setRating(extractRatingValueFn.apply(entity));
            }

            try {
                TmdbMovie movie = tmdbService.getTmdbMovie(dto.getMovieTmdbId());
                dto.setTitle(movie.getTitle());
                dto.setPosterUrl("https://image.tmdb.org/t/p/w500" + movie.getPosterPath());
                dto.setReleaseDate(movie.getReleaseDate());
            } catch (Exception e) {
//                 log.warn("Nie udało się pobrać filmu TMDB o ID {}", dto.getMovieTmdbId());
            }


            return dto;
        });
    }


    public void removeItem(UUID userId, String section, Integer id) {
        switch (section.toLowerCase()) {
            case "favorites" -> favouriteRepository.deleteByUserIdAndMovieTmdbId(userId, id);
            case "watchlist" -> watchlistEntryRepository.deleteByUserIdAndMovieTmdbId(userId, id);
            case "ratings" -> ratingRepository.deleteByUserIdAndMovieTmdbId(userId, id);
            default -> throw new IllegalArgumentException("Nieznana sekcja: " + section);
        }
    }
}
