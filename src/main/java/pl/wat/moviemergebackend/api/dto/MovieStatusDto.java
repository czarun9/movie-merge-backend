// MovieStatusDto.java
package pl.wat.moviemergebackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MovieStatusDto {
    private boolean watched;
    private boolean inWatchlist;
    private boolean favourite;
    private List<RatingDto> ratings;
    private BigDecimal latestRating;
}
