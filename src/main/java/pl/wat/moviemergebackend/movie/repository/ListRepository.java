package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListRepository extends JpaRepository<UserMovieListEntity, UUID> {
    List<UserMovieListEntity> findAllByUserId(UUID userId);
    Optional<UserMovieListEntity> findByUserIdAndId(UUID userId, UUID listId);
}
