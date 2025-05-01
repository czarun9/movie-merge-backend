package pl.wat.moviemergebackend.tmdb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMoviePageResponse{
    private List<TmdbMovie> movies;
    private int totalPages;
}
