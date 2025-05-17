package pl.wat.moviemergebackend.trakt.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wat.moviemergebackend.trakt.service.TraktService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tmdb")
public class TraktController {

    private final TraktService traktService;


}

