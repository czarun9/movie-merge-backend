package pl.wat.moviemergebackend.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "movie_rating")
public class MovieRatingStatusEntity extends BaseMovieStatusEntity{

    @Column(name = "rating_value", nullable = false, precision = 2, scale = 1)
    private BigDecimal ratingValue;
}
