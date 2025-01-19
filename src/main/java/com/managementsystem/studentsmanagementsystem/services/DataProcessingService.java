package com.managementsystem.studentsmanagementsystem.services;

import com.managementsystem.studentsmanagementsystem.models.Role;
import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.RoleRepository;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


@Service
public class DataProcessingService {


private static final String EXCEL_FILE_PATH = "C:/var/log/applications/API/dataprocessing/generated-data.xlsx";

    private static final String CSV_FILE_PATH = "C:/var/log/applications/API/dataprocessing/student-data.csv";

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @Autowired
    private RoleRepository roleRepository; // Assuming you need roles for the user

    // Method for processing data and generating CSV
    public void processData() throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_PATH));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        FileWriter writer = new FileWriter(CSV_FILE_PATH);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Student ID", "Student Name", "Updated Score"));

        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

//            String studentId = row.getCell(0).getStringCellValue(); // Student ID
            Long studentId = (long) row.getCell(0).getNumericCellValue();
            String firstName = row.getCell(1).getStringCellValue(); // First Name
            String lastName = row.getCell(2).getStringCellValue();  // Last Name
            String studentName = firstName + " " + lastName;        // Combine for full name
            long score = (long)  row.getCell(5).getNumericCellValue();    // Original Score
            Long updatedCSVScore = (long) (score + 10);  // Score to be written in CSV
            csvPrinter.printRecord(studentId, studentName, updatedCSVScore);  // Writing to CSV
        }

        csvPrinter.flush();
        csvPrinter.close();
        workbook.close();
    }

    // Method for processing data and uploading it to the database
    public void processDataAndUploadToDatabase(String filePath) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String studentId = row.getCell(0).getStringCellValue(); // Student ID
            String firstName = row.getCell(1).getStringCellValue(); // First Name
            String lastName = row.getCell(2).getStringCellValue();  // Last Name
            int score = (int) row.getCell(5).getNumericCellValue();    // Original Score

            int updatedDBScore = score + 5; // Update score before saving to DB

            // Check if the student exists in the database
            User user = userRepository.findByStudentId(Long.valueOf(studentId));
            if (user != null) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setScore(updatedDBScore);

                // Optionally update roles if necessary
                Set<Role> roles = user.getRoles();
                // Assume you handle roles accordingly
                user.setRoles(roles);

                userRepository.save(user); // Save the updated user data
            }
        }

        workbook.close();
    }

    }
