package ua.nure.korabelska.agrolab.service;

import org.springframework.security.core.userdetails.UserDetails;
import ua.nure.korabelska.agrolab.dto.RegistrationUserDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User registerAdmin(User user);

    User updateUser(Long id, RegistrationUserDto dto) throws UserNotFoundException;

    List<User> getAll();

    User findByUsername(String username) throws UserNotFoundException;

    User findById(Long id);

    void delete(Long id);




//    User editUserById(Long id, UserEditDto userEditDto);
}
