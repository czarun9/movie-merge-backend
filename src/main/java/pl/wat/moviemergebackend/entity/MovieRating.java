package pl.wat.moviemergebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "movie_rating")
public class MovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "movie_tmdb_id", nullable = false)
    private Integer movieTmdbId;

    @Column(name = "rating_value", nullable = false, precision = 2, scale = 1)
    private BigDecimal ratingValue;

    @Column(name = "rated_at", nullable = false)
    private LocalDateTime ratedAt = LocalDateTime.now();
}
