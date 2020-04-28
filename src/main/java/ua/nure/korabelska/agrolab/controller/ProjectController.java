package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.SaveCultureDto;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.security.jwt.JwtTokenProvider;
import ua.nure.korabelska.agrolab.service.CultureService;
import ua.nure.korabelska.agrolab.service.ProjectService;
import ua.nure.korabelska.agrolab.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/project")
@Slf4j
public class ProjectController {

    ProjectService projectService;

    UserService userService;

    CultureService cultureService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, CultureService cultureService, JwtTokenProvider jwtTokenProvider) {
        this.projectService = projectService;
        this.userService = userService;
        this.cultureService = cultureService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/current/manager")
    public ResponseEntity<Object> getProjectForManager(HttpServletRequest request) {

        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User manager;
        try {
            manager = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findProjectByManager(manager);
        log.info("{}",project);
        if(project == null) {
            return new ResponseEntity<>("Project was not found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(project);
    }

    @GetMapping("/current/participant")
    public ResponseEntity<Object> getProjectForParticipant(HttpServletRequest request) {

        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findByMember(user);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("{}",project);
        return ResponseEntity.ok().body(project);
    }

    @GetMapping("/current/{id}/cultures")
    public ResponseEntity<Object> getCulturesByProjectAndCurrentUser(HttpServletRequest request,@PathVariable Long id) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findProjectByUser(user);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project.getCultures());
    }

    @PostMapping("/current/{id}/cultures")
    public ResponseEntity<Object> createCulture(@PathVariable Long id, @RequestBody @Valid SaveCultureDto cultureDto, HttpServletRequest request, BindingResult bindingResult) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        Project project = projectService.findProjectByUser(user);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        Culture culture = cultureService.createCulture(cultureDto,project);
        log.info("{}",culture);
        return ResponseEntity.ok(culture);
    }

    @PostMapping
    public ResponseEntity<Object> createProject(@Valid @RequestBody SaveProjectDto saveProjectDto,
                                                BindingResult bindingResult, HttpServletRequest request) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User manager = null;
        try {
            manager = userService.findByUsername(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        log.info("new project to be created : {} ", saveProjectDto);
        try {
            Project project = projectService.createProject(saveProjectDto, manager);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/current/manager/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable Long id,
                                                @Valid @RequestBody UpdateProjectDto updateProjectDto,
                                                BindingResult bindingResult,HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User manager;
        try {
            manager = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        if(!manager.getManagerInProject().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        log.info("updating project by id : {}", id);
        try {
            Project project = projectService.updateProject(updateProjectDto, id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/current/manager/{id}")
    public ResponseEntity deleteProject(@PathVariable Long id, HttpServletRequest request) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User manager;
        try {
            manager = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        if(!manager.getManagerInProject().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("deleting project by id : {}", id);
        if (projectService.deleteProjectById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }




}
