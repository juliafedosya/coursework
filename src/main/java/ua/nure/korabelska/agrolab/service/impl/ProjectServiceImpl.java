package ua.nure.korabelska.agrolab.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
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

        project.setManager(manager);
        project.setMembers(collectMembers(saveProjectDto.getMembersId()));
        project.setName(saveProjectDto.getName());
        project.setStatus(Status.ACTIVE);

        Project createdProject = projectRepository.save(project);

        manager.setManagerInProject(createdProject);
        userRepository.save(manager);

        Set<User> members = createdProject.getMembers();

                members.stream()
                .forEach(member -> member
                        .setParticipantInProject(createdProject));
//                members.stream()
//                .forEach(member -> userRepository.save(member));

//        log.info("adding members {}",members);
        log.info("manager {}",manager);

        return createdProject;
    }

    @Override
    public Boolean deleteProjectById(Long id) {
        Boolean exists = projectRepository.existsById(id);
        if(exists) {
            log.info("Gonna delete project with id {}", id);
                Project project = projectRepository.findById(id).get();
                project.getManager().setManagerInProject(null);
                project.getMembers().stream().forEach(member -> member.setParticipantInProject(null));
                project.setManager(null);
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
                    .stream()
                    .forEach(member -> member
                            .setParticipantInProject(project));
            projectRepository.save(project);
            return project;
        }
        return null;
    }

    @Override
    public Project findProjectById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        return project;
    }

    public Project findByMember(User member) {
        log.info("member id {}",member.getId());
        Project project = projectRepository.findByMembersId(member.getId());
        log.info("project {}",project);
        return project;
    }

    @Override
    public Project findProjectByManager(User manager) {
        log.info("manager id {}",manager.getId());
        Project project = projectRepository.findByManagerId(manager.getId());
        log.info("project {}",project);
        return project;
    }

    @Override
    public Project findProjectByUser(User user,Long id) {
        Project managerInProject = user.getManagerInProject();
        Project participantInProject = user.getParticipantInProject();

        if(managerInProject != null && managerInProject.getId().equals(id)) {
            return managerInProject;
        }

        return participantInProject;
    }

    @Override
    public Project findProjectByUser(User user) {
        Project managerInProject = user.getManagerInProject();
        Project participantInProject = user.getParticipantInProject();

        if(managerInProject != null) {
            return managerInProject;
        }

        return participantInProject;
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
