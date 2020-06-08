package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.User;


public interface ProjectService {

    Project createProject(SaveProjectDto projectDto, User manager) throws UserNotFoundException;
    Boolean deleteProjectById(Long id);
    Project updateProject(UpdateProjectDto saveProjectDto, Long Id) throws UserNotFoundException;
    Project findProjectById(Long id);
    Project findByMember(User member);
    Project findProjectByManager(User manager);
    Project findProjectByUser(User user,Long id);
    Project findProjectByUser(User user);
    Iterable<Project> findAll();
}
