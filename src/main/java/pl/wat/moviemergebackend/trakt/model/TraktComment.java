package pl.wat.moviemergebackend.trakt.model;


/**
 * Pojedynczy komentarz (review) z Trakt API.
 */
public record TraktComment(
        Integer id,
        String author,
        String content
) {}
