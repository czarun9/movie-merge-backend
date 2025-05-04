package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.moviemergebackend.entity.WatchlistEntry;

import java.util.UUID;

@Repository
public interface WatchlistEntryRepository extends JpaRepository<WatchlistEntry, UUID> {
    boolean existsByUserIdAndMovieTmdbId(UUID userId, Integer movieTmdbId);
}
