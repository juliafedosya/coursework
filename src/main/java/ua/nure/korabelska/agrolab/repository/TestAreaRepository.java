package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.korabelska.agrolab.model.TestArea;

import java.util.List;
import java.util.Optional;

public interface TestAreaRepository extends JpaRepository<TestArea,Long> {

    List<TestArea> findAllByProjectId(Long id);
    TestArea findByIdAndProjectId(Long id, Long projectId);
//    Optional<TestArea> findByIdAndProjectId(Long id, Long projectId);

}
