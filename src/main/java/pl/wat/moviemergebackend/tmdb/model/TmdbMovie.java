package pl.wat.moviemergebackend.tmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.omertron.themoviedbapi.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbMovie implements Serializable {

    private int id;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("poster_path")
    private String posterPath;

    private double popularity;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("_id")
    private String _id;

    private boolean adult;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date")
    private String releaseDate;

    private String title;
    private boolean video;
    private double rating;
    private int runtime;

    @JsonProperty("genres")
    private List<Genre> genres;

    @JsonProperty("original_language")
    private String originalLanguage;

    private String overview;
    private int revenue;
}
