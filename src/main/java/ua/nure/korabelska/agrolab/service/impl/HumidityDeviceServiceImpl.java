package ua.nure.korabelska.agrolab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.korabelska.agrolab.dto.device.DeviceHumidityDto;
import ua.nure.korabelska.agrolab.model.device.HumidityDevice;
import ua.nure.korabelska.agrolab.repository.HumidityDeviceRepository;
import ua.nure.korabelska.agrolab.service.HumidityDeviceService;

@Service
@RequiredArgsConstructor
public class HumidityDeviceServiceImpl implements HumidityDeviceService {

  private final HumidityDeviceRepository humidityDeviceRepository;

  @Override
  public void setDeviceActive(Boolean active, Long id) {
    HumidityDevice current = humidityDeviceRepository.findById(id).orElse(null);
    if (current != null) {
      current.setActive(active);
      humidityDeviceRepository.save(current);
    }
  }

  @Override
  public HumidityDevice updateDeviceData(DeviceHumidityDto deviceHumidityDto, Long id) {
    HumidityDevice current = humidityDeviceRepository.findById(id).orElse(null);
    if(current != null && current.getActive()) {
      current.setHumidity(deviceHumidityDto.getHumidity());
      return humidityDeviceRepository.save(current);
    }
    return current;
  }

  @Override
  public HumidityDevice getDevice(Long id) {
    return humidityDeviceRepository.findById(id).orElse(null);
  }
}
