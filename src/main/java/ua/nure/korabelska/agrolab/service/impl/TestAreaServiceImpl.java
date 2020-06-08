package ua.nure.korabelska.agrolab.service.impl;

import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveTestAreaDto;
import ua.nure.korabelska.agrolab.dto.UpdateTestAreaDto;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.model.TestArea;
import ua.nure.korabelska.agrolab.repository.CultureRepository;
import ua.nure.korabelska.agrolab.repository.TestAreaRepository;
import ua.nure.korabelska.agrolab.service.TestAreaService;

import java.util.List;

@Service
public class TestAreaServiceImpl implements TestAreaService {

    TestAreaRepository testAreaRepository;

    CultureRepository cultureRepository;

    public TestAreaServiceImpl(TestAreaRepository testAreaRepository,CultureRepository cultureRepository) {
        this.testAreaRepository = testAreaRepository;
        this.cultureRepository = cultureRepository;
    }

    @Override
    public TestArea createTestArea(SaveTestAreaDto testAreaDto, Project project) {
        TestArea testArea = new TestArea();
        testArea.setDescription(testAreaDto.getDescription());
        testArea.setProject(project);
        testArea.setStatus(Status.ACTIVE);
        return  testAreaRepository.save(testArea);
    }

    @Override
    public TestArea updateTestArea(UpdateTestAreaDto testAreaDto, TestArea testArea) {
            testArea.setObservationData(testAreaDto.getObservationData());
            Culture culture = cultureRepository.findById(testAreaDto.getCultureId()).get();
            if(culture!=null && (culture.getVisible() ||
                culture.getProject().getId().equals(testArea.getProject().getId()))) {
                testArea.setCulture(culture);
            }
           return testAreaRepository.save(testArea);
    }

    @Override
    public TestArea findTestAreaById(Long id) {
        TestArea testArea = testAreaRepository.findById(id).get();
        return testArea;
    }

    @Override
    public List<TestArea> findTestAreasByProject(Long projectId) {
        List<TestArea> testAreas = testAreaRepository.findAllByProjectId(projectId);
        return testAreas;
    }
}
