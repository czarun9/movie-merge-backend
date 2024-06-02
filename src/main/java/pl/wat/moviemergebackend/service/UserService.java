package pl.wat.moviemergebackend.service;

import pl.wat.moviemergebackend.exception.EmailAlreadyTakenException;
import pl.wat.moviemergebackend.exception.NotFoundException;
import pl.wat.moviemergebackend.model.UserDto;
import pl.wat.moviemergebackend.entity.UserEntity;
import pl.wat.moviemergebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper mapper;
    private final UserRepository userRepository;

    public UserEntity searchByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> findAllUsers() {
        var userEntityList = new ArrayList<>(userRepository.findAll());
        return userEntityList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(final UUID id) {
        var user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("User by id " + id + " was not found")
                );
        return convertToDto(user);
    }

    public UserDto createUser(UserDto userDto, String password) throws NoSuchAlgorithmException{
        UserEntity user = convertToEntity(userDto);
        if (password.isBlank())
            throw new IllegalArgumentException("Password is required.");

        boolean existsEmail = userRepository.selectExistsEmail(user.getEmail());
        if (existsEmail) throw new EmailAlreadyTakenException(
                "Email " + user.getEmail() + " jest zajęty.");

        byte[] salt = createSalt();
        byte[] hashedPassword = createPasswordHash(password, salt);

        user.setStoredSalt(salt);
        user.setStoredHash(hashedPassword);

        userRepository.save(user);
        return convertToDto(user);
    }


    public void updateUser(UUID id, UserDto userDto, String password) throws NoSuchAlgorithmException {
        UserEntity user = findOrThrow(id);
        UserEntity userParam = convertToEntity(userDto);
        user.setEmail(userParam.getEmail());

        if (!password.isBlank()) {
            byte[] salt = createSalt();
            byte[] hashedPassword = createPasswordHash(password, salt);

            user.setStoredSalt(salt);
            user.setStoredHash(hashedPassword);
        }
        userRepository.save(user);
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

    private UserDto convertToDto(UserEntity entity) {
        return mapper.map(entity, UserDto.class);
    }

    private UserEntity convertToEntity(UserDto dto) {
        return mapper.map(dto, UserEntity.class);
    }

    private UserEntity findOrThrow(final UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("User by id " + id + " was not found")
                );
    }
}
