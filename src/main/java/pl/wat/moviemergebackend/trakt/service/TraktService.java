package pl.wat.moviemergebackend.trakt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.Comment;
import com.uwetrottmann.trakt5.entities.Movie;
import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.IdType;
import com.uwetrottmann.trakt5.enums.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.trakt.model.TraktMovie;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class TraktService {

    private final TraktV2 traktApi;
    private final ObjectMapper objectMapper;

    public TraktService(TraktV2 traktApi, ObjectMapper objectMapper) {
        this.traktApi = traktApi;
        this.objectMapper = objectMapper;
    }

    public TraktMovie getTraktMovie(String traktId) {
        try {
            Response<Movie> response = traktApi.movies().summary(traktId, Extended.FULL).execute();
            if (response.isSuccessful() && response.body() != null) {
                Movie movie = response.body();
                return TraktMovie.from(movie);
            } else {
                log.error("Error fetching movie with Trakt ID {}: {}", traktId, response.errorBody());
                throw new RuntimeException("Failed to fetch movie with ID: " + traktId);
            }
        } catch (IOException e) {
            log.error("Exception while fetching movie with Trakt ID {}: {}", traktId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public TraktMovie getTraktMovieByTmdbId(int tmdbId) {
        try {
            Response<List<SearchResult>> searchResponse = traktApi.search()
                    .idLookup(IdType.TMDB, String.valueOf(tmdbId), Type.MOVIE, null, null, null)
                    .execute();

            if (searchResponse.isSuccessful() && searchResponse.body() != null && !searchResponse.body().isEmpty()) {
                SearchResult result = searchResponse.body().get(0);

                if (result.movie != null && result.movie.ids != null && result.movie.ids.trakt != null) {
                    return getTraktMovie(String.valueOf(result.movie.ids.trakt));
                }
            }

            log.error("No Trakt movie found with TMDB ID: {}", tmdbId);
            throw new RuntimeException("No movie found with TMDB ID: " + tmdbId);
        } catch (IOException e) {
            log.error("Exception while fetching movie with TMDB ID {}: {}", tmdbId, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}