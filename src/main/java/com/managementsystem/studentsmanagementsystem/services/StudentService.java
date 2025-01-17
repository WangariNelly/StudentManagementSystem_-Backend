package com.managementsystem.studentsmanagementsystem.services;

import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
 private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public long getTotalStudentsCount() {
        if (userRepository == null) {
            logger.error("/ is null!");
            return 0;
        }
        return userRepository.count();
    }
}
