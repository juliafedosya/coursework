package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.service.ProjectService;

@RestController("/api/v1/project")
@Slf4j
public class ManagerController {

    ProjectService projectService;

    @PostMapping
    public ResponseEntity<Object> createProject(SaveProjectDto saveProjectDto) {

        try {
           Project project =  projectService.createProject(saveProjectDto);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e);
        }


    }

    @GetMapping("/hui")
    public ResponseEntity<String> hui(){
        log.info("Ублюдок, мать твою, а ну иди сюда, говно собачье, решил ко мне лезть? Ты, засранец вонючий, мать твою, а? Ну иди сюда, попробуй меня трахнуть, я тебя сам трахну, ублюдок, онанист чертов, будь ты проклят, иди идиот, трахать тебя и всю семью, говно собачье, жлоб вонючий, дерьмо, сука, падла, иди сюда, мерзавец, негодяй, гад, иди сюда, ты — говно, жопа!");
        return ResponseEntity.ok().body("hui");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(projectService.findProjectById(id));
    }



}
