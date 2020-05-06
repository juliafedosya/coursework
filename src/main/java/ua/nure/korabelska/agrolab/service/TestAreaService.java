package ua.nure.korabelska.agrolab.service;

import org.aspectj.weaver.ast.Test;
import ua.nure.korabelska.agrolab.dto.SaveTestAreaDto;
import ua.nure.korabelska.agrolab.dto.UpdateTestAreaDto;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.TestArea;

import java.util.List;

public interface TestAreaService {

    TestArea createTestArea(SaveTestAreaDto testAreaDto, Project project);
    TestArea updateTestArea(UpdateTestAreaDto testAreaDto, TestArea testArea, Long projectId);
    TestArea findTestAreaByIdAndProjectId(Long id, Long projectId);
    List<TestArea> findTestAreasByProject(Long projectId);
}
