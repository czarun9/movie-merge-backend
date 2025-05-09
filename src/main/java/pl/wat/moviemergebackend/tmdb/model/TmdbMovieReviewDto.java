package pl.wat.moviemergebackend.tmdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wat.moviemergebackend.api.dto.ReviewDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbMovieReviewDto {
    private int totalPages;
    private int totalResults;
    private int page;
    private List<ReviewDto> reviews;
}
