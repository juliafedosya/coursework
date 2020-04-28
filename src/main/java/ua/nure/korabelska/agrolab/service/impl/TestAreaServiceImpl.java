package ua.nure.korabelska.agrolab.service.impl;

import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveTestAreaDto;
import ua.nure.korabelska.agrolab.dto.UpdateTestAreaDto;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.TestArea;
import ua.nure.korabelska.agrolab.service.TestAreaService;

import java.util.List;

@Service
public class TestAreaServiceImpl implements TestAreaService {


    @Override
    public TestArea createTestArea(SaveTestAreaDto testAreaDto, Project project) {
        return null;
    }

    @Override
    public TestArea updateTestArea(UpdateTestAreaDto testAreaDto, Long Id) {
        return null;
    }

    @Override
    public List<TestArea> findTestAreasByIdAndProjectId(Long id, Long projectId) {
        return null;
    }

    @Override
    public List<TestArea> findTestAreasByProject(Long projectId) {
        return null;
    }
}
