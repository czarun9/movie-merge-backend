package pl.wat.moviemergebackend.tmdb.dto;

import com.omertron.themoviedbapi.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenresResponse {
    private List<Genre> genres;
}
