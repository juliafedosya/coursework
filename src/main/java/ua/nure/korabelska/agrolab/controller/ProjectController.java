package ua.nure.korabelska.agrolab.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

  private final ProjectService projectService;

  private final UserService userService;

  private final CultureService cultureService;

  private final TestAreaService testAreaService;

  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/current")
  public ResponseEntity<Object> getProjectGeneral(HttpServletRequest request) {
    String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(project);
  }

  @GetMapping("/current/cultures")
  public ResponseEntity<Object> getCulturesByProjectAndCurrentUser(HttpServletRequest request) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(project.getCultures());
  }

  @GetMapping("/current/cultures/{cultureId}")
  public ResponseEntity<?> getCultureByProjectAndCurrentUserAndId(HttpServletRequest request,
      @PathVariable Long cultureId) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    Optional<Culture> culture = project.getCultures().stream()
        .filter(c -> c.getId().equals(cultureId)).findFirst();
    return culture.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/current/testAreas")
  public ResponseEntity<Object> getTestAreasByProjectAndCurrentUser(HttpServletRequest request) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(project.getTestAreas());
  }

  @GetMapping("/current/testAreas/{testAreaId}")
  public ResponseEntity<?> getTestAreaByProjectAndCurrentUserAndId(HttpServletRequest request,
      @PathVariable Long testAreaId) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    Optional<TestArea> testArea = project.getTestAreas().stream()
        .filter(t -> t.getId().equals(testAreaId)).findFirst();
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
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    log.info("new project to be created : {} ", saveProjectDto);
    try {
      Project project = projectService.createProject(saveProjectDto, manager);
      return new ResponseEntity<>(project, HttpStatus.CREATED);
    } catch (UserNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @PostMapping("/current/cultures")
  public ResponseEntity<Object> createCulture(@RequestBody @Valid SaveCultureDto cultureDto,
      HttpServletRequest request, BindingResult bindingResult) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    Culture culture = cultureService.createCulture(cultureDto, project);
    log.info("{}", culture);
    return ResponseEntity.ok(culture);
  }

  @PostMapping("/current/testAreas")
  public ResponseEntity<Object> createTestArea(@RequestBody @Valid SaveTestAreaDto testAreaDto,
      HttpServletRequest request, BindingResult bindingResult) {
    String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    TestArea testArea = testAreaService.createTestArea(testAreaDto, project);
    log.info("{}", testArea);
    return new ResponseEntity<>(testArea, HttpStatus.CREATED);
  }

  @PatchMapping("/current")
  public ResponseEntity<Object> updateProject(
      @Valid @RequestBody UpdateProjectDto updateProjectDto,
      BindingResult bindingResult, HttpServletRequest request) {

    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    String username = resolveUsername(request);
    User current;
    try {
      current = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    Project currentProject = current.getParticipantInProject();
    log.info("updating project by id : {}", currentProject.getId());
    try {
      Project project = projectService.updateProject(updateProjectDto, currentProject, current);
      return new ResponseEntity<>(project, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PatchMapping("/current/cultures/{cultureId}")
  public ResponseEntity<Object> updateCulture(@PathVariable Long cultureId,
      @RequestBody @Valid UpdateCultureDto cultureDto,
      HttpServletRequest request, BindingResult bindingResult) {
    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    Project project = projectService.findProjectByUser(user);
    if (project == null) {
      return ResponseEntity.notFound().build();
    }
    Culture culture = project.getCultures().stream().filter(c -> c.getId().equals(cultureId))
        .findFirst().orElse(null);
    if (culture == null) {
      return ResponseEntity.notFound().build();
    }
    cultureService.updateCulture(culture, cultureDto);
    log.info("{}", culture);
    return ResponseEntity.ok(culture);
  }

  @PatchMapping("/current/testAreas/{testAreaId}")
  public ResponseEntity<Object> updateTestArea(@PathVariable Long testAreaId,
      @RequestBody @Valid UpdateTestAreaDto testAreaDto,
      HttpServletRequest request, BindingResult bindingResult) {

    String username = resolveUsername(request);
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    if (bindingResult.hasErrors()) {
      Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    TestArea testArea = testAreaService.findTestAreaById(testAreaId);
    if (testArea == null || !testArea.getProject().getId()
        .equals(user.getParticipantInProject().getId())) {
      return ResponseEntity.notFound().build();
    }
    testArea = testAreaService.updateTestArea(testAreaDto, testArea);
    return ResponseEntity.ok(testArea);
  }


  @DeleteMapping("/current/{id}")
  public ResponseEntity<?> deleteProject(@PathVariable Long id, HttpServletRequest request) {
    String username = resolveUsername(request);
    User current;
    try {
      current = userService.findByUsername(username);
    } catch (UserNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    if (!current.getParticipantInProject().getId().equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    log.info("deleting project by id : {}", id);
    if (projectService.deleteProjectById(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  private String resolveUsername(HttpServletRequest request) {
    return jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
  }

}
