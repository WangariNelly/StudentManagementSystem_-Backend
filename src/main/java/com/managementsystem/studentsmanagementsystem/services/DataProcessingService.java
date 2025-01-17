package com.managementsystem.studentsmanagementsystem.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class DataProcessingService {
private static final String EXCEL_FILE_PATH = "C:/var/log/applications/API/dataprocessing/generated-data.xlsx";

    private static final String CSV_FILE_PATH = "path/to/csv/file.csv";


    public void processData() throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_PATH));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        FileWriter writer = new FileWriter(CSV_FILE_PATH);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Student ID", "Student Name", "Updated Score"));

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Assuming columns are: ID, Name, Score
            String studentId = row.getCell(0).getStringCellValue(); // Student ID
            String firstName = row.getCell(1).getStringCellValue(); // First Name
            String lastName = row.getCell(2).getStringCellValue();  // Last Name
            String studentName = firstName + " " + lastName;        // Combine for full name
            double score = row.getCell(5).getNumericCellValue();    // Score

            // Update score by adding 10
            double updatedScore = score + 10;

            // Write to CSV
            csvPrinter.printRecord(studentId, studentName, updatedScore);
        }

        // Close resources
        csvPrinter.flush();
        csvPrinter.close();
        workbook.close();
    }

    }
