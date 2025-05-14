package pl.wat.moviemergebackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wat.moviemergebackend.user.entity.UserEntity;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link UserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private UUID id;
    private String email;
    private String password;
}