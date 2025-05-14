package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.movie.entity.MovieRatingStatusEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRatingStatusEntity, UUID> {
    List<MovieRatingStatusEntity> findByUserIdAndMovieTmdbId(UUID userId, Integer movieTmdbId);
    Page<MovieRatingStatusEntity> findByUserIdOrderByCreatedAtDesc(UUID user_id, Pageable pageable);
    void deleteByUserIdAndId(UUID user_id, UUID id);
}
