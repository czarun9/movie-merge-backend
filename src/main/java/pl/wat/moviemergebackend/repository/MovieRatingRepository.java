package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.entity.MovieRating;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, UUID> {
    List<MovieRating> findByUserIdAndMovieTmdbId(UUID userId, Integer movieTmdbId);
}
