package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.entity.FavouriteMovieEntity;

import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<FavouriteMovieEntity, UUID> {
    boolean existsByUserIdAndMovieTmdbId(UUID userId, Integer tmdbId);
    void deleteByUserIdAndMovieTmdbId(UUID userId, Integer tmdbId);
}
