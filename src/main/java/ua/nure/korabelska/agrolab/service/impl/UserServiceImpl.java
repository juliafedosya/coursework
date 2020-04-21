package ua.nure.korabelska.agrolab.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.RegistrationUserDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Role;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.repository.RoleRepository;
import ua.nure.korabelska.agrolab.repository.UserRepository;
import ua.nure.korabelska.agrolab.service.UserService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {

        if(roleRepository.findByName("ROLE_USER") == null) {
            Role role = new Role();
            role.setName("ROLE_USER");
            role.setCreated(new Date(1L));
            role.setUpdated(new Date(1L));
            role.setStatus(Status.ACTIVE);
            roleRepository.save(role);
        }

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN Register – user: {} successfully created", registeredUser);

        return registeredUser;
    }

    @Override
    public User registerAdmin(User user) {

        if(roleRepository.findByName("ROLE_USER") == null) {
            Role role = new Role();
            role.setName("ROLE_USER");
            role.setCreated(new Date(1L));
            role.setUpdated(new Date(1L));
            role.setStatus(Status.ACTIVE);
            roleRepository.save(role);
        }

        if(roleRepository.findByName("ROLE_ADMIN") == null) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setCreated(new Date(1L));
            role.setUpdated(new Date(1L));
            role.setStatus(Status.ACTIVE);
            roleRepository.save(role);
        }

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleAdmin);
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN Register – ADMIN: {} successfully created", registeredUser);

        return registeredUser;
    }

    @Override
    public User updateUser(Long id, RegistrationUserDto dto) throws UserNotFoundException {
        if(userRepository.existsById(id)) {
            User current = userRepository.findById(id).get();
            current.setUsername(dto.getUsername());
            current.setEmail(dto.getEmail());
            current.setFirstName(dto.getFirstName());
            current.setLastName(dto.getLastName());
            current.setPassword(dto.getPassword());
           current = userRepository.save(current);
           return current;
        }
        throw new UserNotFoundException(id);
    }


    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User result = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted",id);
    }
}
