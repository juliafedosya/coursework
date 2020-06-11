package ua.nure.korabelska.agrolab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.SaveTestAreaDto;
import ua.nure.korabelska.agrolab.dto.update.UpdateTestAreaDto;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.model.device.AcidityDevice;
import ua.nure.korabelska.agrolab.model.device.HumidityDevice;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.Status;
import ua.nure.korabelska.agrolab.model.TestArea;
import ua.nure.korabelska.agrolab.repository.AcidityDeviceRepository;
import ua.nure.korabelska.agrolab.repository.CultureRepository;
import ua.nure.korabelska.agrolab.repository.HumidityDeviceRepository;
import ua.nure.korabelska.agrolab.repository.TestAreaRepository;
import ua.nure.korabelska.agrolab.service.TestAreaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestAreaServiceImpl implements TestAreaService {

    final TestAreaRepository testAreaRepository;

    final CultureRepository cultureRepository;

    final HumidityDeviceRepository humidityDeviceRepository;

    final AcidityDeviceRepository acidityDeviceRepository;

    @Override
    public TestArea createTestArea(SaveTestAreaDto testAreaDto, Project project) {
        TestArea testArea = new TestArea();
        testArea.setDescription(testAreaDto.getDescription());
        testArea.setProject(project);
        testArea.setStatus(Status.ACTIVE);
        testArea =  testAreaRepository.save(testArea);
        HumidityDevice device = new HumidityDevice();
        device.setTestArea(testArea);
        device.setId(testArea.getId());
        device.setHumidity(0);
        device.setActive(true);
        AcidityDevice acidityDevice = new AcidityDevice();
        acidityDevice.setTestArea(testArea);
        acidityDevice.setId(testArea.getId());
        acidityDevice.setAcidity(0);
        acidityDevice.setActive(true);

        humidityDeviceRepository.save(device);
        acidityDeviceRepository.save(acidityDevice);
        return testArea;
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
