package pl.wat.moviemergebackend.user.dto;

import java.util.UUID;

public record User(
        UUID id,
        String email,
        String password
) {
}