package ua.nure.korabelska.agrolab.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveCultureDto;
import ua.nure.korabelska.agrolab.dto.UpdateCultureDto;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.repository.CultureRepository;
import ua.nure.korabelska.agrolab.service.CultureService;

import java.util.List;

@Service
@Slf4j
public class CultureServiceImpl implements CultureService {

    private final CultureRepository cultureRepository;

    @Autowired
    public CultureServiceImpl(CultureRepository cultureRepository) {
        this.cultureRepository = cultureRepository;
    }

    @Override
    public Culture createCulture(SaveCultureDto cultureDto, Project project) {
        log.info("culture dto - {}",cultureDto);
        Culture culture = new Culture();
        culture.setDescription(cultureDto.getDescription());
        culture.setName(cultureDto.getName());
        culture.setPlantDivision(cultureDto.getPlantDivision());
        culture.setProject(project);
        culture.setVisible(cultureDto.getVisible());
        culture.setStatus(Status.ACTIVE);
        culture = cultureRepository.save(culture);
        log.info("culture  - {}",culture);
        return culture;
    }

    @Override
    public Culture updateCulture(UpdateCultureDto cultureDto, Long id) {
        Culture culture = cultureRepository.findById(id).get();
        if(culture != null) {
            culture.setVisible(cultureDto.getVisible());
            culture.setDescription(cultureDto.getDescription());
            culture.setName(cultureDto.getName());
          return cultureRepository.save(culture);
        }
        return null;
    }

    @Override
    public Culture findCultureByIdAndVisible(Long id) {
        Culture culture = cultureRepository.findByIdAndVisible(id,true);
        return culture;
    }

    @Override
    public List<Culture> findAllCulturesVisible() {
        List<Culture> cultures = cultureRepository.findByVisibleTrue();
        return cultures;
    }

    @Override
    public List<Culture> findCulturesByProject(Long projectId) {
        List<Culture> cultures = cultureRepository.findByProjectId(projectId);
        return cultures;
    }
}
