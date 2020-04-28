package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.SaveCultureDto;
import ua.nure.korabelska.agrolab.dto.UpdateCultureDto;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;

import java.util.List;

public interface CultureService {


    Culture createCulture(SaveCultureDto cultureDto, Project project);
    Culture updateCulture(UpdateCultureDto cultureDto, Long Id);
    Culture findCultureByIdAndVisible(Long id);
    List<Culture> findAllCulturesVisible();
    List<Culture> findCulturesByProject(Long projectId);

}
