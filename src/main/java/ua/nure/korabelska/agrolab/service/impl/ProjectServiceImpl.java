package ua.nure.korabelska.agrolab.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.update.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.repository.ProjectRepository;
import ua.nure.korabelska.agrolab.repository.UserRepository;
import ua.nure.korabelska.agrolab.service.ProjectService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project createProject(SaveProjectDto saveProjectDto, User manager) throws UserNotFoundException {
        Project project = new Project();

        project.setMembers(collectMembers(saveProjectDto.getMembersId()));
        project.addMember(manager);
        project.setName(saveProjectDto.getName());
        project.setStatus(Status.ACTIVE);

        manager.setParticipantInProject(project);
        userRepository.save(manager);

        Set<User> members = project.getMembers();

                members.stream()
                .forEach(member -> member
                        .setParticipantInProject(project));
        log.info("manager {}",manager);

        Project createdProject = projectRepository.save(project);

        return createdProject;
    }

    @Override
    public Boolean deleteProjectById(Long id) {
        Boolean exists = projectRepository.existsById(id);
        if(exists) {
            log.info("Gonna delete project with id {}", id);
                Project project = projectRepository.findById(id).get();
                project.getMembers().forEach(member -> member.setParticipantInProject(null));
                project.setMembers(null);
                projectRepository.deleteById(id);
        }
        return exists;
    }

    @Override
    public Project updateProject(UpdateProjectDto updateProjectDto, Long id) throws UserNotFoundException {
        if(projectRepository.existsById(id)){
            Project project = projectRepository.findById(id).get();
            project.setMembers(collectMembers(updateProjectDto.getMembersId()));
            project.setName(updateProjectDto.getName());
            project.getMembers()
                    .forEach(member -> member
                            .setParticipantInProject(project));
            projectRepository.save(project);
            return project;
        }
        return null;
    }

    @Override
    public Project updateProject(UpdateProjectDto projectDto, Project project, User current)
        throws UserNotFoundException {
        project.setMembers(collectMembers(projectDto.getMembersId()));
        project.setName(projectDto.getName());
        project.getMembers()
            .forEach(member -> member
                .setParticipantInProject(project));
        project.addMember(current);
        projectRepository.save(project);
        return project;
    }

    @Override
    public Project findProjectById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        return project;
    }

    @Override
    public Project findProjectByUser(User user) {
        Project participantInProject = user.getParticipantInProject();
        return projectRepository.findById(participantInProject.getId()).orElse(null);
    }

    @Override
    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    private Set<User> collectMembers(Set<Long> members) throws UserNotFoundException {
        Set<User> users = new HashSet<>();

        for (Long memberId : members) {
            users.add(userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException(memberId)));
        }
        return users;
    }

}
