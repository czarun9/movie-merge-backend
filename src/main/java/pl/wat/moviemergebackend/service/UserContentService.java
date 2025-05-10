package pl.wat.moviemergebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.api.dto.ListItemDto;
import pl.wat.moviemergebackend.entity.BaseMovieStatusEntity;
import pl.wat.moviemergebackend.entity.MovieRatingStatusEntity;
import pl.wat.moviemergebackend.repository.FavouriteRepository;
import pl.wat.moviemergebackend.repository.MovieRatingRepository;
import pl.wat.moviemergebackend.repository.WatchlistEntryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserContentService {

    private final FavouriteRepository favouriteRepository;
    private final WatchlistEntryRepository watchlistEntryRepository;
    private final MovieRatingRepository ratingRepository;
//    private final ReviewRepository reviewRepository;

    public Page<ListItemDto> getUserFavorites(UUID userId, Pageable pageable) {
        return favouriteRepository.findAllByUserId(userId, pageable)
                .map(this::mapToListItemDto);
    }

    public Page<ListItemDto> getUserWatchlist(UUID userId, Pageable pageable) {
        return watchlistEntryRepository.findAllByUserId(userId, pageable)
                .map(this::mapToListItemDto);
    }

    public Page<ListItemDto> getUserRatings(UUID userId, Pageable pageable) {
        return ratingRepository.findAllByUserId(userId, pageable)
                .map(this::mapToListItemDto);
    }

//    public Page<ListItemDto> getUserReviews(UUID userId, Pageable pageable) {
//        return reviewRepository.findAllByUserId(userId, pageable)
//                .map(this::mapToListItemDto);
//    }

    public void removeItem(UUID userId, String section, Integer id) {
        switch (section.toLowerCase()) {
            case "favorites" -> favouriteRepository.deleteByUserIdAndMovieTmdbId(userId, id);
            case "watchlist" -> watchlistEntryRepository.deleteByUserIdAndMovieTmdbId(userId, id);
            case "ratings" -> ratingRepository.deleteByUserIdAndMovieTmdbId(userId, id);
//            case "reviews" -> reviewRepository.deleteByUserIdAndMovieId(userId, id);
            default -> throw new IllegalArgumentException("Nieznana sekcja: " + section);
        }
    }

    private ListItemDto mapToListItemDto(Object entity) {
        ListItemDto dto = new ListItemDto();

        if (entity instanceof BaseMovieStatusEntity) {
            BaseMovieStatusEntity baseEntity = (BaseMovieStatusEntity) entity;
            dto.setMovieTmdbId(baseEntity.getMovieTmdbId());
            dto.setCreatedAt(baseEntity.getCreatedAt());
        }

        if (entity instanceof MovieRatingStatusEntity) {
            MovieRatingStatusEntity ratingEntity = (MovieRatingStatusEntity) entity;
            dto.setRating(ratingEntity.getRatingValue());
        }

        return dto;
    }
}
