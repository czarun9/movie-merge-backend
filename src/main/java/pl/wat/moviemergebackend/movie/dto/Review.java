package pl.wat.moviemergebackend.movie.dto;

public record Review(
        String id,
        String author,
        String content,
        String url,
        String platform
) {
}
