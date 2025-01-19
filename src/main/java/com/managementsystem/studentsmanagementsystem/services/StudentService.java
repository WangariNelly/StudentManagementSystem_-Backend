package com.managementsystem.studentsmanagementsystem.services;

import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public long getTotalStudentsCount() {
//        if (userRepository == null) {
//            logger.error("/ is null!");
//            return 0;
//        }
        return userRepository.count();
    }

    // Get filtered students with pagination
    public Page<User> getFilteredStudents(Long studentId, String className, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return userRepository.findByStudentIdAndClassNameContainingAndDobBetween(studentId, className, startDate, endDate, pageable);
    }

//    public Page<User> getFilteredStudents(Pageable pageable) {
//        return userRepository.findAll(pageable);  // No filters applied
//    }



    //     Soft delete student by setting status to 0
    public boolean deleteStudent(Long studentId) {
        Optional<User> student = userRepository.findById(studentId);
        if (student.isPresent()) {
            User user = student.get();
            user.setStatus(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    //update
    public User updateStudent(Long studentId, User updatedStudent, String photoPath) throws IOException {
        Optional<User> student = userRepository.findById(studentId);
        if (student.isPresent()) {
            User user = student.get();
            user.setFirstName(updatedStudent.getFirstName());
            user.setLastName(updatedStudent.getLastName());
            user.setDob(updatedStudent.getDob());
            user.setClassName(updatedStudent.getClassName());
            user.setScore(updatedStudent.getScore());
            user.setPhotoPath(photoPath);
            return userRepository.save(user);
        }
        return null;
    }

    //photo upload
    public String handlePhotoUpload(Long studentId, byte[] photoData, String filename) throws IOException {
        String newFilename = studentId + "-" + filename;
        String photoPath = "C:/var/log/applications/API/StudentPhotos/" + newFilename;
        File photoFile = new File(photoPath);
        Files.write(Paths.get(photoFile.toURI()), photoData);
        return photoPath;
    }

    // Method to export students to Excel
    public byte[] exportStudentsToExcel(List<User> students) throws IOException {
        // Create a workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Student ID", "First Name", "Last Name", "Status", "DOB", "Class", "Score"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // Populate data rows
        int rowNum = 1;
        for (User student : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getFirstName());
            row.createCell(2).setCellValue(student.getLastName());
            row.createCell(3).setCellValue(student.getStatus());
            row.createCell(4).setCellValue(student.getDob().toString()); // Ensure the DOB is formatted correctly
            row.createCell(5).setCellValue(student.getClassName());
            row.createCell(6).setCellValue(student.getScore());
        }

        // Write the workbook to a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        // Return the byte array representing the Excel file
        return byteArrayOutputStream.toByteArray();
    }
}
