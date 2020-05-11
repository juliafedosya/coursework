package ua.nure.korabelska.agrolab.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ua.nure.korabelska.agrolab.model.Role;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto {

        private Long id;
        private String username;
        private String lastName;
        private String firstName;
        private String email;
        private String status;
        private boolean isAdmin;

        public User toUser() {
            User user = new User();

            user.setId(id);
            user.setEmail(email);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setStatus(Status.valueOf(status));

            return user;
        }

        public static AdminUserDto fromUser(User user) {
            AdminUserDto adminUserDto = new AdminUserDto();

            adminUserDto.setId(user.getId());
            adminUserDto.setUsername(user.getUsername());
            adminUserDto.setFirstName(user.getFirstName());
            adminUserDto.setLastName(user.getLastName());
            adminUserDto.setEmail(user.getEmail());
            adminUserDto.setStatus(user.getStatus().name());
            adminUserDto.setAdmin(user.getRoles().stream().filter(role -> role.getName().equals("ROLE_ADMIN")).findFirst().isPresent());

            return adminUserDto;
        }
}
