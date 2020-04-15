package ua.nure.korabelska.agrolab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.korabelska.agrolab.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
    User findByUsername(String name);
}
