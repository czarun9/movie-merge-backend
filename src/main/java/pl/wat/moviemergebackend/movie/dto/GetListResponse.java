package pl.wat.moviemergebackend.movie.dto;

import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetListResponse(UUID id, String name, LocalDateTime createdAt) {

    public static GetListResponse fromEntity(UserMovieListEntity entity) {
        return new GetListResponse(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt()
        );
    }

}
