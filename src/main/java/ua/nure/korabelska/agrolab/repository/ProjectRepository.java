package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.korabelska.agrolab.model.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
