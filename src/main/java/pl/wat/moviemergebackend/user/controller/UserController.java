package pl.wat.moviemergebackend.user.controller;

import org.springframework.http.ResponseEntity;
import pl.wat.moviemergebackend.exception.EmailAlreadyTakenException;
import pl.wat.moviemergebackend.user.dto.User;
import pl.wat.moviemergebackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/users")
    public Iterable<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/api/v1/users/{id}")
    public User getUserById(@PathVariable("id") UUID id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/api/v1/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") UUID id) {
        userService.removeUserById(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@Valid @RequestBody User user) throws NoSuchAlgorithmException {
        return userService.createUser(user, user.password());
    }

    @PutMapping("/api/v1/users/{id}")
    public void putUser(@PathVariable("id") UUID id, @Valid @RequestBody User user) throws NoSuchAlgorithmException {
        userService.updateUser(id, user, user.password());
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<String> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
