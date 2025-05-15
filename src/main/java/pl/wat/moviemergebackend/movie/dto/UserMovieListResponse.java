package pl.wat.moviemergebackend.movie.dto;

import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserMovieListResponse(UUID id, String name, LocalDateTime createdAt) {

    public static UserMovieListResponse fromEntity(UserMovieListEntity entity) {
        return new UserMovieListResponse(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt()
        );
    }

}
