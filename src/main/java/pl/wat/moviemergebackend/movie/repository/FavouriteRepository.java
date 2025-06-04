package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.movie.entity.MovieFavouriteStatusEntity;
import pl.wat.moviemergebackend.user.entity.UserEntity;

import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<MovieFavouriteStatusEntity, UUID> {
    boolean existsByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserIdAndId(UUID user_id, UUID id);
    Page<MovieFavouriteStatusEntity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
