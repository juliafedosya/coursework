package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.RegistrationUserDto;
import ua.nure.korabelska.agrolab.dto.AdminUserDto;
import ua.nure.korabelska.agrolab.dto.UserDto;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/register")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegistrationUserDto registrationUserDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        User user = userService.register(registrationUserDto.toUser());
        log.info("i am here in register");
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody @Valid RegistrationUserDto registrationUserDto,BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        User user = userService.registerAdmin(registrationUserDto.toUser());

        AdminUserDto result = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
