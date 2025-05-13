package pl.wat.moviemergebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.entity.MovieFavouriteStatusEntity;
import pl.wat.moviemergebackend.entity.UserEntity;

import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<MovieFavouriteStatusEntity, UUID> {
    boolean existsByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    Page<MovieFavouriteStatusEntity> findAllByUserId(UUID userId, Pageable pageable);

    void deleteByUserIdAndMovieTmdbId(UUID user_id, Integer movieTmdbId);

    Page<MovieFavouriteStatusEntity> findByUserId(UUID user_id, Pageable pageable);
}
