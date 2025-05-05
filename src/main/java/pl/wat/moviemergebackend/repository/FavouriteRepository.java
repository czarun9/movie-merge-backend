package pl.wat.moviemergebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.entity.FavouriteMovieEntity;
import pl.wat.moviemergebackend.entity.UserEntity;

import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<FavouriteMovieEntity, UUID> {
    boolean existsByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);
    void deleteByUserAndMovieTmdbId(UserEntity user, Integer movieTmdbId);

}
