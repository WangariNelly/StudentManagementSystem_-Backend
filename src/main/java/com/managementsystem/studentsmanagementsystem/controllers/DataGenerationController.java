
package com.managementsystem.studentsmanagementsystem.controllers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
public class DataGenerationController {

    private static final String BASE_DIRECTORY = "C:/var/log/applications/API/dataprocessing";

    private static final Logger logger = LoggerFactory.getLogger(DataGenerationController.class);


    @GetMapping("/api/data/generate")
    public ResponseEntity<?> generateData(@RequestParam int records) {
        logger.info("Data generation started for {} records.", records);

        try {
            createDirectoryIfNotExists(BASE_DIRECTORY);
            String filePath = BASE_DIRECTORY + "/generated-data.xlsx";
            writeExcelFile(filePath, records);
            logger.info("Excel file generated successfully at: {}", filePath);
            return ResponseEntity.ok("Excel file generated successfully at: " + filePath);
        } catch (IOException e) {
            logger.error("Error generating Excel file", e);
            return ResponseEntity.status(500).body("Error generating Excel file: " + e.getMessage());
        }
}

    /**
     * Create an Excel file with student data and write it to the specified file path.
     *
     * @param filePath The path where the Excel file will be saved.
     * @param records  The number of records to generate.
     * @throws IOException If an error occurs during file creation or writing.
     */
    private void writeExcelFile(String filePath, int records) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            // Add header row
            String[] columns = {"Student ID", "First Name", "Last Name", "DOB", "Class", "Score", "Status", "PhotoPath"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }

            // Populate data rows
            Random random = new Random();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < records; i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(i + 1); // Student ID
                dataRow.createCell(1).setCellValue(generateRandomString(3, 8)); // First Name
                dataRow.createCell(2).setCellValue(generateRandomString(3, 8)); // Last Name
                dataRow.createCell(3).setCellValue(generateRandomDOB(dateFormatter)); // DOB
                dataRow.createCell(4).setCellValue("Class" + (random.nextInt(5) + 1)); // Class
                dataRow.createCell(5).setCellValue(random.nextInt(31) + 55); // Score
                dataRow.createCell(6).setCellValue(1); // Status (Active)
                dataRow.createCell(7).setCellValue("N/A"); // PhotoPath
            }

            // Write workbook to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        }
    }

    /**
     * Generate a random string with a length between the specified minimum and maximum.
     *
     * @param min Minimum length.
     * @param max Maximum length.
     * @return A randomly generated string.
     */
    private String generateRandomString(int min, int max) {
        int length = min + new Random().nextInt(max - min + 1);
        return new Random().ints('a', 'z' + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Generate a random date of birth between 01-01-2000 and 31-12-2010.
     *
     * @param dateFormatter The formatter to format the date as a string.
     * @return A formatted random date of birth.
     */
    private String generateRandomDOB(DateTimeFormatter dateFormatter) {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2010, 12, 31);

        long days = startDate.until(endDate).getDays();
        return startDate.plusDays(new Random().nextInt((int) days + 1)).format(dateFormatter);
    }

    /**
     * Create a directory if it does not already exist.
     *
     * @param directoryPath The path of the directory to create.
     * @throws IOException If an error occurs during directory creation.
     */
    private void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
