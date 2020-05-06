package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.korabelska.agrolab.model.Culture;

import java.util.List;

public interface CultureRepository extends JpaRepository<Culture,Long> {
    public List<Culture> findByVisibleTrue();
    public Culture findByIdAndVisible(Long id, Boolean visible);
    public Culture findByIdAndProjectId(Long id, Long projectId);
    public List<Culture> findByProjectId(Long projectId);
}
