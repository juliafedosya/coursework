package ua.nure.korabelska.agrolab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.RegistrationUserDto;
import ua.nure.korabelska.agrolab.dto.UserDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.security.jwt.JwtTokenProvider;
import ua.nure.korabelska.agrolab.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/current")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user = null;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = "/current")
    public ResponseEntity<Object> editUser(@RequestBody RegistrationUserDto userEditDto, HttpServletRequest request) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user = null;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        User editedUser = null;

        try {
            editedUser = userService.updateUser(user.getId(), userEditDto);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        UserDto result = UserDto.fromUser(editedUser);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
