package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.services.DataProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class DataProcessingController {

    private static final String CSV_FILE_PATH = "path/to/csv/file.csv";;
    private DataProcessingService dataProcessingService;

    @GetMapping("/api/data/process")
    public ResponseEntity<String> processData() {
        try {
            dataProcessingService.processData();
            return ResponseEntity.ok("Data processed successfully. CSV file saved at: " + CSV_FILE_PATH);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing data: " + e.getMessage());
        }
    }

}
