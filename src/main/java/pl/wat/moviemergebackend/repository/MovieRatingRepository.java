package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.entity.MovieRatingStatusEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRatingStatusEntity, UUID> {
    List<MovieRatingStatusEntity> findByUserIdAndMovieTmdbId(UUID userId, Integer movieTmdbId);
}
