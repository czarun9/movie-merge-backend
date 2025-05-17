package pl.wat.moviemergebackend.movie.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ListItem(
        UUID id,
        Integer movieTmdbId,
        String title,
        String releaseDate,
        String posterUrl,
        BigDecimal rating,
        LocalDateTime createdAt
) {}
