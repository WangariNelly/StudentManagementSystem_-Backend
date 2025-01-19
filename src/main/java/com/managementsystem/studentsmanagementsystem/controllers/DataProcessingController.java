package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.services.DataProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataProcessingController {

    private static final String CSV_FILE_PATH = "path/to/csv/file.csv";;

    @Autowired
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
//uploadind data to db
    public ResponseEntity<String> uploadToDatabase(@RequestParam String filePath) {
        try {
            dataProcessingService.processDataAndUploadToDatabase(filePath);
            return ResponseEntity.ok("Data uploaded to the database successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading data to DB: " + e.getMessage());
        }
    }
}
