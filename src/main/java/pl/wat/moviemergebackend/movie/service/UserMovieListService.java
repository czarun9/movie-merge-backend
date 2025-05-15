package pl.wat.moviemergebackend.movie.service;

import jakarta.transaction.Transactional;
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

    @Transactional
    public void deleteList(UUID userId, UUID listId) {
        UserMovieListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to delete this list.");
        }

        listRepository.delete(list);
    }

}

