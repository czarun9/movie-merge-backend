package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.entity.WatchedMovie;

import java.util.UUID;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, UUID> {
    boolean existsByUserIdAndMovieTmdbId(UUID userId, Integer movieTmdbId);
}
