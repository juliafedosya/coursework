package ua.nure.korabelska.agrolab.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ua.nure.korabelska.agrolab.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationUserDto {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 6,max = 50)
    private String password;

    public User toUser() {
        User user = new User();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setCreated(timestamp);
        user.setUpdated(timestamp);

        return user;
    }

}
