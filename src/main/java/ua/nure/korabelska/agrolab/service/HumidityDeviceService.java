package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.device.DeviceHumidityDto;
import ua.nure.korabelska.agrolab.model.device.HumidityDevice;

public interface HumidityDeviceService {

  void setDeviceActive(Boolean active, Long id);
  HumidityDevice updateDeviceData(DeviceHumidityDto deviceHumidityDto, Long id);
  HumidityDevice getDevice(Long id);
}
