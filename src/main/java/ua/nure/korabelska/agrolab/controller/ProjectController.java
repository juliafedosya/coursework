package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ua.nure.korabelska.agrolab.dto.*;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.TestArea;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.security.jwt.JwtTokenProvider;
import ua.nure.korabelska.agrolab.service.CultureService;
import ua.nure.korabelska.agrolab.service.ProjectService;
import ua.nure.korabelska.agrolab.service.TestAreaService;
import ua.nure.korabelska.agrolab.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/project")
@Slf4j
public class ProjectController {

    private static final String DEVICES_ENDPOINT = "http://localhost:8090/devices/";

    private final ProjectService projectService;

    private final UserService userService;

    private final CultureService cultureService;

    private final TestAreaService testAreaService;

    private final JwtTokenProvider jwtTokenProvider;

    private final RestTemplate restTemplate;

    private final HttpComponentsClientHttpRequestFactory requestFactory;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, CultureService cultureService,TestAreaService testAreaService, JwtTokenProvider jwtTokenProvider) {
        this.projectService = projectService;
        this.userService = userService;
        this.cultureService = cultureService;
        this.testAreaService = testAreaService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = new RestTemplate();
        this.requestFactory = new HttpComponentsClientHttpRequestFactory();
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

    @GetMapping("/current")
    public ResponseEntity<Object> getProjectGeneral(HttpServletRequest request) {
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
        return ResponseEntity.ok(project);
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
        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project.getCultures());
    }

    @GetMapping("/current/{id}/cultures/{cultureId}")
    public ResponseEntity<?> getCultureByProjectAndCurrentUserAndId(HttpServletRequest request,@PathVariable Long id,@PathVariable Long cultureId) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<Culture> culture = project.getCultures().stream().filter(c -> c.getId().equals(id)).findFirst();
        return culture.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/current/{id}/testAreas")
    public ResponseEntity<Object> getTestAreasByProjectAndCurrentUser(HttpServletRequest request,@PathVariable Long id) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project.getTestAreas());
    }

    @GetMapping("/current/{id}/testAreas/{testAreaId}")
    public ResponseEntity<?> getTestAreaByProjectAndCurrentUserAndId(HttpServletRequest request, @PathVariable Long id, @PathVariable Long testAreaId) {
        String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<TestArea> testArea = project.getTestAreas().stream().filter(t -> t.getId().equals(testAreaId)).findFirst();
       return testArea.map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
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

        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        Culture culture = cultureService.createCulture(cultureDto,project);
        log.info("{}",culture);
        return ResponseEntity.ok(culture);
    }

    @PostMapping("/current/{id}/testAreas")
    public ResponseEntity<Object> createTestArea(@PathVariable Long id, @RequestBody @Valid SaveTestAreaDto testAreaDto, HttpServletRequest request, BindingResult bindingResult) {
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

        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        TestArea testArea = testAreaService.createTestArea(testAreaDto,project);
        log.info("{}",testArea);
        return new ResponseEntity<>(testArea,HttpStatus.CREATED);
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

    @PatchMapping("/current/{id}/cultures/{cultureId}")
    public ResponseEntity<Object> updateCulture(@PathVariable Long id, @PathVariable Long cultureId,
                                                @RequestBody @Valid UpdateCultureDto cultureDto,
                                                HttpServletRequest request, BindingResult bindingResult) {
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

        Project project = projectService.findProjectByUser(user,id);
        if(project == null) {
            return ResponseEntity.notFound().build();
        }
        Culture culture = cultureService.updateCulture(cultureDto,cultureId);
        if(culture == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("{}",culture);
        return ResponseEntity.ok(culture);
    }

    @PatchMapping("/current/{id}/testAreas/{testAreaId}")
    public ResponseEntity<Object> updateTestArea(@PathVariable("id") Long projectId, @PathVariable Long testAreaId,
                                                @RequestBody @Valid UpdateTestAreaDto testAreaDto,
                                                HttpServletRequest request, BindingResult bindingResult) {

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

        TestArea testArea = testAreaService.findTestAreaByIdAndProjectId(testAreaId,projectId);
        if(testArea == null) {
            return ResponseEntity.notFound().build();
        }
        testArea = testAreaService.updateTestArea(testAreaDto,testArea,projectId);
        return ResponseEntity.ok(testArea);
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
