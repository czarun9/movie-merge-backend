package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;

import java.util.List;
import java.util.UUID;

public interface ListRepository extends JpaRepository<UserMovieListEntity, UUID> {
    List<UserMovieListEntity> findAllByUserId(UUID userId);
}
