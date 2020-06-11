package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.korabelska.agrolab.model.device.HumidityDevice;

@Repository
public interface HumidityDeviceRepository extends JpaRepository<HumidityDevice,Long> {

}
