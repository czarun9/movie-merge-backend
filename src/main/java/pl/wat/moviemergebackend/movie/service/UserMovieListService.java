package pl.wat.moviemergebackend.movie.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.wat.moviemergebackend.movie.dto.CreateListRequest;
import pl.wat.moviemergebackend.movie.dto.GetListResponse;
import pl.wat.moviemergebackend.movie.entity.UserMovieListEntity;
import pl.wat.moviemergebackend.movie.entity.UserMovieListItemEntity;
import pl.wat.moviemergebackend.movie.repository.ListRepository;
import pl.wat.moviemergebackend.user.entity.UserEntity;
import pl.wat.moviemergebackend.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMovieListService {

    private final ListRepository listRepository;
    private final UserRepository userRepository;

    public List<GetListResponse> getUserLists(UUID userId) {
        return listRepository.findAllByUserId(userId)
                .stream()
                .map(GetListResponse::fromEntity)
                .toList();
    }

    public GetListResponse getUserList(UUID listId, UUID userId) {
        return listRepository.findByUserIdAndId(userId, listId)
                .map(GetListResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));
    }

    public GetListResponse createList(UUID userId, String listName) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserMovieListEntity list = new UserMovieListEntity();
        list.setName(listName);
        list.setUser(user);

        listRepository.save(list);

        return new GetListResponse(list.getId(), list.getName(), list.getCreatedAt());
    }

    @Transactional
    public GetListResponse createListWithMovie(UUID userId, CreateListRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserMovieListEntity list = new UserMovieListEntity();
        list.setName(request.name());
        list.setUser(user);

        UserMovieListItemEntity item = new UserMovieListItemEntity();
        item.setMovieTmdbId(request.initialMovieId());
        item.setUser(user);
        item.setList(list);
        item.setCreatedAt(LocalDateTime.now());

        list.getItems().add(item);

        listRepository.save(list);

        return new GetListResponse(list.getId(), list.getName(), list.getCreatedAt());
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


    @Transactional
    public void addMovieToList(UUID userId, UUID listId, Integer movieTmdbId) {
        UserMovieListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new NoSuchElementException("Lista nie istnieje"));

        if (!list.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Brak dostępu do tej listy");
        }

        boolean alreadyExists = list.getItems().stream()
                .anyMatch(item -> item.getMovieTmdbId().equals(movieTmdbId));
        if (alreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Film już istnieje na liście");
        }

        UserMovieListItemEntity item = new UserMovieListItemEntity();
        item.setMovieTmdbId(movieTmdbId);
        item.setUser(list.getUser());
        item.setList(list);
        item.setCreatedAt(LocalDateTime.now());

        list.getItems().add(item);
        listRepository.save(list);
    }

    @Transactional
    public void removeMovieFromList(UUID userId, UUID listId, Integer movieTmdbId) {
        UserMovieListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new SecurityException("You don't have permission to modify this list");
        }

        UserMovieListItemEntity itemToRemove = list.getItems().stream()
                .filter(item -> item.getMovieTmdbId().equals(movieTmdbId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie not found in the list"));

        list.getItems().remove(itemToRemove);
    }
}

