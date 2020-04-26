package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;

import java.util.List;

public interface CultureService {


    Culture createCulture(SaveProjectDto projectDto) throws UserNotFoundException;
    Culture updateCulture(UpdateProjectDto saveProjectDto, Long Id) throws UserNotFoundException;
    Culture findCultureByIdAndVisible(Long id);
    List<Culture> findAllCulturesVisible();
    List<Culture> findCulturesByProject(Long projectId);

}
