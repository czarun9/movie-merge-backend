package pl.wat.moviemergebackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.api.dto.MovieStatusDto;
import pl.wat.moviemergebackend.entity.FavouriteMovieEntity;
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

    public MovieStatusDto getMovieStatus(UUID userId, Integer tmdbId) {
        boolean watched = watchedRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId);
        boolean inWatchlist = watchlistRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId);
        boolean favourite = favouriteRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId);

        List<RatingDto> ratings = ratingRepo.findByUserIdAndMovieTmdbId(userId, tmdbId)
                .stream()
                .map(r -> {
                    RatingDto dto = new RatingDto();
                    dto.setValue(r.getRatingValue());
                    dto.setRatedAt(r.getRatedAt());
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
        if (isFavourite) {
            if (!favouriteRepo.existsByUserIdAndMovieTmdbId(userId, tmdbId)) {
                FavouriteMovieEntity entity = new FavouriteMovieEntity();
                entity.setUserId(userId);
                entity.setMovieTmdbId(tmdbId);
                favouriteRepo.save(entity);
            }
        } else {
            favouriteRepo.deleteByUserIdAndMovieTmdbId(userId, tmdbId);
        }
    }

}
