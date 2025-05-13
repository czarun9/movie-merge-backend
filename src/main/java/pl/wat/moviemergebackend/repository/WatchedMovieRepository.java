package pl.wat.moviemergebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.entity.UserEntity;
import pl.wat.moviemergebackend.entity.MovieWatchedStatusEntity;

import java.util.UUID;

@Repository
public interface WatchedMovieRepository extends JpaRepository<MovieWatchedStatusEntity, UUID> {
    boolean existsByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    Page<MovieWatchedStatusEntity> findByUserIdOrderByCreatedAtDesc(UUID user_id, Pageable pageable);
    void deleteByUserIdAndId(UUID user_id, UUID id);

}
