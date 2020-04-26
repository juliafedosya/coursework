package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.korabelska.agrolab.model.TestArea;

import java.util.List;

public interface TestAreaRepository extends JpaRepository<TestArea,Long> {

    List<TestArea> findAllByProjectId(Long id);
    List<TestArea> findAllByCultureId(Long id);

}
