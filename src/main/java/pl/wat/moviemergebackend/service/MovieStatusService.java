package pl.wat.moviemergebackend.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto;
import pl.wat.moviemergebackend.entity.*;
import pl.wat.moviemergebackend.repository.FavouriteRepository;
import pl.wat.moviemergebackend.repository.MovieRatingRepository;
import pl.wat.moviemergebackend.repository.WatchedMovieRepository;
import pl.wat.moviemergebackend.repository.WatchlistEntryRepository;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto.RatingDto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieStatusService {

    private final WatchedMovieRepository watchedRepo;
    private final WatchlistEntryRepository watchlistRepo;
    private final MovieRatingRepository ratingRepo;
    private final FavouriteRepository favouriteRepo;
    private final EntityManager entityManager;

    public MovieStatusDto getMovieStatus(UUID userId, Integer tmdbId) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        boolean watched = watchedRepo.existsByUserAndMovieTmdbId(user, tmdbId);
        boolean inWatchlist = watchlistRepo.existsByUserAndMovieTmdbId(user, tmdbId);
        boolean favourite = favouriteRepo.existsByUserAndMovieTmdbId(user, tmdbId);

        List<RatingDto> ratings = ratingRepo.findByUserIdAndMovieTmdbId(user.getId(), tmdbId)
                .stream()
                .map(r -> {
                    RatingDto dto = new RatingDto();
                    dto.setValue(r.getRatingValue());
                    dto.setRatedAt(r.getCreatedAt());
                    return dto;
                })
                .sorted(Comparator.comparing(RatingDto::getRatedAt))
                .collect(Collectors.toList());

        BigDecimal latest = ratings.isEmpty() ? null : ratings.get(ratings.size() - 1).getValue();

        MovieStatusDto dto = new MovieStatusDto();
        dto.setWatched(watched);
        dto.setInWatchlist(inWatchlist);
        dto.setFavourite(favourite);
        dto.setRatings(ratings);
        dto.setLatestRating(latest);

        return dto;
    }

    @Transactional
    public void setFavoriteStatus(UUID userId, Integer tmdbId, boolean isFavourite) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        if (isFavourite) {
            if (!favouriteRepo.existsByUserAndMovieTmdbId(user, tmdbId)) {
                MovieFavouriteStatusEntity entity = new MovieFavouriteStatusEntity();
                entity.setUser(user);
                entity.setMovieTmdbId(tmdbId);
                favouriteRepo.save(entity);
            }
        } else {
            favouriteRepo.deleteByUserAndMovieTmdbId(user, tmdbId);
        }
    }

    @Transactional
    public void setWatchedStatus(UUID userId, Integer tmdbId, boolean isWatched) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        if (isWatched) {
            if (!watchedRepo.existsByUserAndMovieTmdbId(user, tmdbId)) {
                MovieWatchedStatusEntity entity = new MovieWatchedStatusEntity();
                entity.setUser(user);
                entity.setMovieTmdbId(tmdbId);
                watchedRepo.save(entity);
            }
        } else {
            watchedRepo.deleteByUserAndMovieTmdbId(user, tmdbId);
        }
    }

    @Transactional
    public void setAddedToWatchlistStatus(UUID userId, Integer tmdbId, boolean isAddedToWatchlist) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        if (isAddedToWatchlist) {
            if (!watchlistRepo.existsByUserAndMovieTmdbId(user, tmdbId)) {
                MovieWatchlistStatusEntity entity = new MovieWatchlistStatusEntity();
                entity.setUser(user);
                entity.setMovieTmdbId(tmdbId);
                watchlistRepo.save(entity);
            }
        } else {
            watchlistRepo.deleteByUserAndMovieTmdbId(user, tmdbId);
        }
    }

}
