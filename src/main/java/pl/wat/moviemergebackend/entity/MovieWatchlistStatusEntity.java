package pl.wat.moviemergebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "watchlist_status",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","movie_tmdb_id"}))
public class MovieWatchlistStatusEntity extends BaseMovieStatusEntity {

}