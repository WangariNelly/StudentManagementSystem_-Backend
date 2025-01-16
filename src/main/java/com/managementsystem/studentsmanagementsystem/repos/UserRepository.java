package com.managementsystem.studentsmanagementsystem.repos;

import com.managementsystem.studentsmanagementsystem.models.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    User findByFirstNameAndLastName(String firstName, String lastName);
}
