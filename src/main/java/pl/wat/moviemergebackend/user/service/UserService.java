package pl.wat.moviemergebackend.user.service;

import pl.wat.moviemergebackend.exception.EmailAlreadyTakenException;
import pl.wat.moviemergebackend.exception.NotFoundException;
import pl.wat.moviemergebackend.user.dto.User;
import pl.wat.moviemergebackend.user.entity.UserEntity;
import pl.wat.moviemergebackend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final ObjectMapper mapper;
    private final UserRepository userRepository;

    public UserEntity searchByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        var userEntityList = new ArrayList<>(userRepository.findAll());
        return userEntityList
                .stream()
                .map(entity -> mapper.convertValue(entity, User.class))
                .collect(Collectors.toList());
    }

    public User findUserById(final UUID id) {
        var user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("User by id " + id + " was not found")
                );
        return mapper.convertValue(user, User.class);
    }

    public User createUser(User user, String password) throws NoSuchAlgorithmException {
        UserEntity userEntity = convertToEntity(user);
        if (password.isBlank())
            throw new IllegalArgumentException("Password is required.");

        boolean existsEmail = userRepository.selectExistsEmail(userEntity.getEmail());
        if (existsEmail) throw new EmailAlreadyTakenException(
                "Email " + userEntity.getEmail() + " jest zajęty.");

        byte[] salt = createSalt();
        byte[] hashedPassword = createPasswordHash(password, salt);

        userEntity.setStoredSalt(salt);
        userEntity.setStoredHash(hashedPassword);

        userRepository.save(userEntity);
        return mapper.convertValue(userEntity, User.class);
    }

    public void updateUser(UUID id, User user, String password) throws NoSuchAlgorithmException {
        UserEntity userEntity = findOrThrow(id);
        UserEntity userParam = convertToEntity(user);
        userEntity.setEmail(userParam.getEmail());

        if (!password.isBlank()) {
            byte[] salt = createSalt();
            byte[] hashedPassword = createPasswordHash(password, salt);

            userEntity.setStoredSalt(salt);
            userEntity.setStoredHash(hashedPassword);
        }
        userRepository.save(userEntity);
    }

    public void removeUserById(UUID id) {
        findOrThrow(id);
        userRepository.deleteById(id);
    }

    private byte[] createSalt() {
        var random = new SecureRandom();
        var salt = new byte[128];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] createPasswordHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    private UserEntity convertToEntity(User dto) {
        return mapper.convertValue(dto, UserEntity.class);
    }

    private UserEntity findOrThrow(final UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("User by id " + id + " was not found")
                );
    }
}
