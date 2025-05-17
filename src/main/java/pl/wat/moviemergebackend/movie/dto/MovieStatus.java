package pl.wat.moviemergebackend.movie.dto;

import java.math.BigDecimal;
import java.util.List;

public record MovieStatus(
        boolean watched,
        boolean inWatchlist,
        boolean favourite,
        List<Rating> ratings,
        BigDecimal latestRating
) {
}
