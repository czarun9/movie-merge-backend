package pl.wat.moviemergebackend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListItemDto {
    private Integer movieTmdbId;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private BigDecimal rating;
    private LocalDateTime createdAt;
}

