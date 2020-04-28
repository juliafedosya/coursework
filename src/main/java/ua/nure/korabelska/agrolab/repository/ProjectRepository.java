package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.korabelska.agrolab.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByMembersId(Long memberId);

    Project findByManagerId(Long managerId);


}

