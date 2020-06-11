package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.device.DeviceAcidityDto;
import ua.nure.korabelska.agrolab.model.device.AcidityDevice;

public interface AcidityDeviceService {

  void setDeviceActive(Boolean active, Long id);
  AcidityDevice updateDeviceData(DeviceAcidityDto deviceAcidityDto, Long id);
  AcidityDevice getDevice(Long id);
}
