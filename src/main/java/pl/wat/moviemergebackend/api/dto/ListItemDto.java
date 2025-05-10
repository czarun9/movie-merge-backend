package pl.wat.moviemergebackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ListItemDto {
    private Integer movieTmdbId;
    private String title;
    private LocalDateTime createdAt;
    private BigDecimal rating;
    private String posterUrl;
    private String content;
    private String author;
}

