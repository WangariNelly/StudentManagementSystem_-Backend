package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/report")
    public ResponseEntity<Page<User>> getStudentsReport(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        Pageable pageable = PageRequest.of(page, size);
//        Page<User> studentsPage = studentService.getFilteredStudents(studentId, className, start, end, pageable);
        Page<User> studentsPage = studentService.getFilteredStudents(pageable);

        return new ResponseEntity<>(studentsPage, HttpStatus.OK);
    }


    @GetMapping
    public List<User> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    //Delete student
    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
        boolean deleted = studentService.deleteStudent(studentId);
        if (deleted) {
            return new ResponseEntity<>("Student deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
    }

    //Updating student details
    @PutMapping("/update/{studentId}")
    public ResponseEntity<User> updateStudent(@PathVariable Long studentId,
                                              @RequestBody User updatedStudent,
                                              @RequestParam(required = false) MultipartFile photo) throws IOException {

        String photoPath = updatedStudent.getPhotoPath();

        if (photo != null && !photo.isEmpty()) {
            byte[] photoBytes = photo.getBytes();
            photoPath = studentService.handlePhotoUpload(studentId, photoBytes, photo.getOriginalFilename());
        }

        User updatedUser = studentService.updateStudent(studentId, updatedStudent, photoPath);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudentsReport() throws IOException{
        List<User> students = studentService.getFilteredStudents(Pageable.unpaged()).getContent();
        byte[] excelFile = studentService.exportStudentsToExcel(students);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=students_report.xlsx");
        return new ResponseEntity<>(new byte[0], headers, HttpStatus.OK);
    }
}
