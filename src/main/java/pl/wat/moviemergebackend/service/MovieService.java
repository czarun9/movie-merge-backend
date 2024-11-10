package pl.wat.moviemergebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.entity.MovieEntity;
import pl.wat.moviemergebackend.repository.MovieRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<MovieEntity> getMovieById(UUID id) {
        return movieRepository.findById(id);
    }

    public MovieEntity createMovie(MovieEntity movie) {
        return movieRepository.save(movie);
    }

    public Optional<MovieEntity> updateMovie(UUID id, MovieEntity updatedMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setGenre(updatedMovie.getGenre());
                    movie.setYear(updatedMovie.getYear());
                    movie.setRating(updatedMovie.getRating());
                    movie.setImageUrl(updatedMovie.getImageUrl());
                    return movieRepository.save(movie);
                });
    }

    public boolean deleteMovie(UUID id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movieRepository.delete(movie);
                    return true;
                })
                .orElse(false);
    }
}

