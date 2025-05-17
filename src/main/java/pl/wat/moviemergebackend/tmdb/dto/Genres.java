package pl.wat.moviemergebackend.tmdb.dto;

import com.omertron.themoviedbapi.model.Genre;
import java.util.List;

public record Genres(List<Genre> genres) {
}
