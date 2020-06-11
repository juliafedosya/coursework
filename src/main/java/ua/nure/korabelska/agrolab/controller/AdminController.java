package ua.nure.korabelska.agrolab.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.AdminUserDto;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.service.AcidityDeviceService;
import ua.nure.korabelska.agrolab.service.HumidityDeviceService;
import ua.nure.korabelska.agrolab.service.ProjectService;
import ua.nure.korabelska.agrolab.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/admin")
public class AdminController {

    private final UserService userService;

    private final ProjectService projectService;

    private final AcidityDeviceService acidityDeviceService;

    private final HumidityDeviceService humidityDeviceService;

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AdminUserDto result = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<User> users = userService.getAll();
        List<AdminUserDto> userDtos = users.stream().map(AdminUserDto::fromUser).collect(Collectors.toList());
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping("/devices/acidity/{id}")
    public ResponseEntity<?> setAcidDeviceStatus(@RequestParam() Boolean active,
        @PathVariable Long id) {
        acidityDeviceService.setDeviceActive(active,id);
        if(active) {
            return ResponseEntity.ok("Enabled.");
        } else {
            return ResponseEntity.ok("Disabled.");
        }
    }

    @GetMapping("/devices/acidity/{id}")
    public ResponseEntity<?> setHumidityDeviceStatus(@RequestParam() Boolean active,
        @PathVariable Long id) {
        humidityDeviceService.setDeviceActive(active,id);
        if(active) {
            return ResponseEntity.ok("Enabled.");
        } else {
            return ResponseEntity.ok("Disabled.");
        }
    }

    @GetMapping("/projects")
    public Iterable<Project> getAllProjects() {
        return projectService.findAll();
    }
}
