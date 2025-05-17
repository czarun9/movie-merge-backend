package pl.wat.moviemergebackend.movie.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Rating(
        BigDecimal value,
        LocalDateTime ratedAt
) {
}
