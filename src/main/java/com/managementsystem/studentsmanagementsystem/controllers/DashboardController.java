package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import com.managementsystem.studentsmanagementsystem.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students/count")
    @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Long> getTotalStudentsCount() {
        try{
        long totalStudents = studentService.getTotalStudentsCount();
        return ResponseEntity.ok(totalStudents);
    } catch (Exception e) {
            return ResponseEntity.status(500).body(0L);
        }
    }
}
