package pl.wat.moviemergebackend.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "watched_status",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","movie_tmdb_id"}))
public class MovieWatchedStatusEntity extends BaseMovieStatusEntity {

}
