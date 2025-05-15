package pl.wat.moviemergebackend.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "user_movie_list_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"list_id", "movie_tmdb_id"})
)
@Getter
@Setter
public class UserMovieListItemEntity extends BaseMovieStatusEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "list_id")
    private UserMovieListEntity list;
}
