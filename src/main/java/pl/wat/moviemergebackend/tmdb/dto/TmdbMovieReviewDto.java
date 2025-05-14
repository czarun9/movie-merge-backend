package pl.wat.moviemergebackend.tmdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wat.moviemergebackend.movie.dto.ReviewDto;

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
