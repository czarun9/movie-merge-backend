package pl.wat.moviemergebackend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private String id;
    private String author;
    private String content;
    private String url;
    private String platform;
}
