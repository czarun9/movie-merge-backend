package pl.wat.moviemergebackend.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.moviemergebackend.movie.entity.UserMovieListItemEntity;

import java.util.UUID;

public interface ListItemRepository extends JpaRepository<UserMovieListItemEntity, UUID> {
    Page<UserMovieListItemEntity> findByUser_IdAndList_IdOrderByCreatedAtDesc(UUID userId, UUID listId, Pageable pageable);
}