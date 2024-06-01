package pl.wat.moviemergebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link pl.wat.moviemergebackend.entity.UserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private UUID id;
    private String email;
    private String mobileNumber;
    private String password;
}