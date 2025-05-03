package pl.wat.moviemergebackend.tmdb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscoverSearchFilters {
    private String genre;
    private Integer rating;
    private int page;
    private String releaseDate;
}
