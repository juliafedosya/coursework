package ua.nure.korabelska.agrolab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.loops.ProcessPath;
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
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project createProject(SaveProjectDto saveProjectDto) throws UserNotFoundException {
        Project project = new Project();
        User manager = userRepository.findByUsername(saveProjectDto.getManagerUsername())
                .orElseThrow(() -> new UserNotFoundException(saveProjectDto.getManagerUsername()));

        project.setManager(manager);
        project.setMembers(collectMembers(saveProjectDto.getUsernames()));
        project.setName(saveProjectDto.getName());
        project.setStatus(Status.ACTIVE);

        Project createdProject = projectRepository.save(project);

        return createdProject;
    }

    @Override
    public Boolean deleteProjectById(Long id) {
        Boolean exists = projectRepository.existsById(id);
        if(exists) {
            projectRepository.deleteById(id);
        }
        return exists;
    }

    @Override
    public Project updateProject(UpdateProjectDto updateProjectDto, Long id) throws UserNotFoundException {
        if(projectRepository.existsById(id)){
            Project project = projectRepository.findById(id).get();
            project.setMembers(collectMembers(updateProjectDto.getUsernames()));
            project.setName(updateProjectDto.getName());
            return project;
        }
        return null;
    }

    @Override
    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    private Set<User> collectMembers(Set<String> members) throws UserNotFoundException {
        Set<User> users = new HashSet<>();

        for (String memberName : members) {
            users.add(userRepository.findByUsername(memberName).orElseThrow(() -> new UserNotFoundException(memberName)));
        }
        return users;
    }

}
