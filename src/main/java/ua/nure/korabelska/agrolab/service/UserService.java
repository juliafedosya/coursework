package ua.nure.korabelska.agrolab.service;

import org.springframework.security.core.userdetails.UserDetails;
import ua.nure.korabelska.agrolab.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User registerAdmin(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);




//    User editUserById(Long id, UserEditDto userEditDto);
}
