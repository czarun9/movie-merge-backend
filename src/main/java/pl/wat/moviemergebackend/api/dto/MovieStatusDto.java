// MovieStatusDto.java
package pl.wat.moviemergebackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MovieStatusDto {
    private boolean watched;
    private boolean inWatchlist;
    private boolean favourite;
    private List<RatingDto> ratings;
    private BigDecimal latestRating;


    @Getter
    @Setter
    public static class RatingDto {
        private BigDecimal value;
        private LocalDateTime ratedAt;
    }
}
