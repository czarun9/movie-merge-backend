package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.user.entity.UserEntity;
import pl.wat.moviemergebackend.movie.entity.MovieWatchlistStatusEntity;

import java.util.UUID;

@Repository
public interface WatchlistEntryRepository extends JpaRepository<MovieWatchlistStatusEntity, UUID> {
    boolean existsByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserIdAndId(UUID user_id, UUID id);
    Page<MovieWatchlistStatusEntity> findByUserIdOrderByCreatedAtDesc(UUID user_id, Pageable pageable);
}
