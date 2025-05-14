package pl.wat.moviemergebackend.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "favourite_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_tmdb_id"})
})
@Getter
@Setter
public class MovieFavouriteStatusEntity extends BaseMovieStatusEntity {
}
