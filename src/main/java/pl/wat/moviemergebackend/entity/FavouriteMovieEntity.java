package pl.wat.moviemergebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "favourite_movies", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_tmdb_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class FavouriteMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "movie_tmdb_id", nullable = false)
    private Integer movieTmdbId;
}
