package ua.nure.korabelska.agrolab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.device.DeviceAcidityDto;
import ua.nure.korabelska.agrolab.model.device.AcidityDevice;
import ua.nure.korabelska.agrolab.repository.AcidityDeviceRepository;
import ua.nure.korabelska.agrolab.service.AcidityDeviceService;

@Service
@RequiredArgsConstructor
public class AcidityDeviceServiceImpl implements AcidityDeviceService {

  private final AcidityDeviceRepository deviceRepository;

  @Override
  public void setDeviceActive(Boolean active, Long id) {

    AcidityDevice current = deviceRepository.findById(id).orElse(null);
    if (current != null) {
      current.setActive(active);
      deviceRepository.save(current);
    }

  }

  @Override
  public AcidityDevice updateDeviceData(DeviceAcidityDto deviceAcidityDto, Long id) {
    AcidityDevice current = deviceRepository.findById(id).orElse(null);
    if(current != null && current.getActive()) {
      current.setAcidity(deviceAcidityDto.getAcidity());
      return deviceRepository.save(current);
    }
    return current;
  }

  @Override
  public AcidityDevice getDevice(Long id) {
    return deviceRepository.findById(id).orElse(null);
  }
}
