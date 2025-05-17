package pl.wat.moviemergebackend.security;

public record AuthenticationRequest(String email, String password) {
}
