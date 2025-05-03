package pl.wat.moviemergebackend.tmdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.methods.TmdbMovies;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.omertron.themoviedbapi.tools.HttpTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yamj.api.common.http.SimpleHttpClientBuilder;
import pl.wat.moviemergebackend.tmdb.dto.GenresResponse;
import pl.wat.moviemergebackend.tmdb.dto.TmdbMoviePageResponse;
import pl.wat.moviemergebackend.tmdb.model.DiscoverSearchFilters;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovie;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovieDiscover;
import pl.wat.moviemergebackend.tmdb.model.TmdbMovieInfo;

import java.util.List;

@Slf4j
@Service
@Component
public class TmdbService {

    private final TmdbMovies tmdbMovies;
    private final TheMovieDbApi theMovieDbApi;
    private final ObjectMapper objectMapper;

    public TmdbService(@Value("${themoviedb.api.key}") String TMDBKEY, ObjectMapper objectMapper) throws MovieDbException {
        this.objectMapper = objectMapper;
        HttpClient httpClient = new SimpleHttpClientBuilder().build();
        this.tmdbMovies = new TmdbMovies(TMDBKEY, new HttpTools(httpClient));
        this.theMovieDbApi = new TheMovieDbApi(TMDBKEY);
    }

    public TmdbMovie getTmdbMovie(int movieId) throws MovieDbException, JsonProcessingException {
        MovieInfo movieInfo = tmdbMovies.getMovieInfo(movieId, "pl");
        return objectMapper.convertValue(movieInfo, TmdbMovieInfo.class);
    }

    public TmdbMoviePageResponse getDiscoverTmdbMovies(DiscoverSearchFilters filters) throws MovieDbException, JsonProcessingException {
        Discover discover = new Discover()
                .page(filters.getPage())
                .voteCountGte(300)
                .language("pl");

        Discover filteredDiscover = applyFilters(discover, filters);
        return executeDiscover(filteredDiscover);
    }

    private TmdbMoviePageResponse executeDiscover(Discover discover) throws MovieDbException {
        ResultList<MovieBasic> resultList = theMovieDbApi.getDiscoverMovies(discover);

        List<MovieBasic> movies = resultList.getResults();
        int totalPages = resultList.getTotalPages();

        List<TmdbMovie> convertedMovies = objectMapper.convertValue(
                movies,
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, TmdbMovieDiscover.class)
        );

        return new TmdbMoviePageResponse(convertedMovies, totalPages);
    }

    private Discover applyFilters(Discover discover, DiscoverSearchFilters filters) {
        if (filters.getGenre() != null && !filters.getGenre().isEmpty())
            discover.withGenres(filters.getGenre());
        return discover;
    }

    public GenresResponse getGenres() throws MovieDbException {
        ResultList<Genre> resultList = theMovieDbApi.getGenreMovieList("pl");
        return new GenresResponse(resultList.getResults());
    }

}
