package pl.wat.moviemergebackend.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.wat.moviemergebackend.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_movie_lists")
@Getter
@Setter
public class UserMovieListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMovieListItemEntity> items;
}
