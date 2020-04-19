package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.AuthenticationRequestDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.security.jwt.JwtTokenProvider;
import ua.nure.korabelska.agrolab.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "api/v1/auth")
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody AuthenticationRequestDto requestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            log.info("hello");
            log.info(requestDto.toString());
            String username = requestDto.getUsername();
            User user = null;
            try {
                user = userService.findByUsername(username);
            } catch (UserNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));


            log.info("hello 2");

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/a")
    public ResponseEntity<List<User>> find() {
        return ResponseEntity.ok().body(userService.getAll());
    }


}
