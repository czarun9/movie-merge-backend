package pl.wat.moviemergebackend.exception;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}