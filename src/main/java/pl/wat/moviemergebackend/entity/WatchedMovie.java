package pl.wat.moviemergebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "watched_movie",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","movie_tmdb_id"}))
public class WatchedMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "movie_tmdb_id", nullable = false)
    private Integer movieTmdbId;

    @Column(name = "watched_at", nullable = false)
    private LocalDateTime watchedAt = LocalDateTime.now();
}
