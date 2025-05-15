package pl.wat.moviemergebackend.movie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.moviemergebackend.movie.dto.UserMovieListRequest;
import pl.wat.moviemergebackend.movie.dto.UserMovieListResponse;
import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;
import pl.wat.moviemergebackend.movie.repository.ListRepository;
import pl.wat.moviemergebackend.user.entity.UserEntity;
import pl.wat.moviemergebackend.user.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMovieListService {

    private final ListRepository listRepository;
    private final UserRepository userRepository;

    public UserMovieListResponse createList(UUID userId, UserMovieListRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserMovieListEntity list = new UserMovieListEntity();
        list.setName(request.name());
        list.setUser(user);

        listRepository.save(list);

        return new UserMovieListResponse(list.getId(), list.getName());
    }
}

