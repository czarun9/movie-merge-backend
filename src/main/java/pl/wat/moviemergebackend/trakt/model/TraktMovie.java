package pl.wat.moviemergebackend.trakt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uwetrottmann.trakt5.entities.Movie;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TraktMovie(
        String title,
        Integer year,
        TraktIds ids,
        String certification,
        String tagline,
        String overview,
        String released,
        Integer runtime,
        String trailer,
        String homepage,
        String language,
        List<String> genres,
        Double rating,
        Integer votes,
        String updatedAt,
        List<String> availableTranslations,
        List<TraktComment> comments
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TraktIds(
            Integer trakt,
            String slug,
            String imdb,
            Integer tmdb
    ) {}

    public static TraktMovie from(Movie movie, List<TraktComment> comments) {
        TraktIds ids = null;
        if (movie.ids != null) {
            ids = new TraktIds(
                    movie.ids.trakt,
                    movie.ids.slug,
                    movie.ids.imdb,
                    movie.ids.tmdb
            );
        }

        return new TraktMovie(
                movie.title,
                movie.year,
                ids,
                movie.certification,
                movie.tagline,
                movie.overview,
                movie.released != null ? movie.released.toString() : null,
                movie.runtime,
                movie.trailer,
                movie.homepage,
                movie.language,
                movie.genres,
                movie.rating,
                movie.votes,
                movie.updated_at != null ? movie.updated_at.toString() : null,
                movie.available_translations,
                comments
        );
    }
}
