package pl.wat.moviemergebackend.controller;

import pl.wat.moviemergebackend.model.AuthenticationRequest;
import pl.wat.moviemergebackend.model.AuthenticationResponse;
import pl.wat.moviemergebackend.service.ApplicationUserDetailsService;
import pl.wat.moviemergebackend.util.JwtUtil;
import pl.wat.moviemergebackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
class LoginController {
    private final JwtUtil jwtTokenUtil;
    private final ApplicationUserDetailsService userDetailsService;

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse login(@RequestBody AuthenticationRequest req) throws Exception {
        UserEntity user;
        try {
            user = userDetailsService.authenticate(req.getEmail(), req.getPassword());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }
}