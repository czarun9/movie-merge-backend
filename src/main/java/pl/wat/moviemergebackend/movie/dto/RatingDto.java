package pl.wat.moviemergebackend.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDto {
    private BigDecimal value;
    private LocalDateTime ratedAt;
}
