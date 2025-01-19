package com.managementsystem.studentsmanagementsystem.repos;

import com.managementsystem.studentsmanagementsystem.models.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByStudentId(Long studentId);

    Page<User> findByStudentIdAndClassNameContainingAndDobBetween(
            Long studentId, String className, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
