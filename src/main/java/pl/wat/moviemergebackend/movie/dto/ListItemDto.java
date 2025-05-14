package pl.wat.moviemergebackend.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListItemDto {
    private UUID id;
    private Integer movieTmdbId;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private BigDecimal rating;
    private LocalDateTime createdAt;
}

