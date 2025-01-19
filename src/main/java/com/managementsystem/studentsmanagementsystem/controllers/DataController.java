package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import com.managementsystem.studentsmanagementsystem.services.DataService;
import net.datafaker.Faker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder _passwordEncoder;

    public DataController(PasswordEncoder passwordEncoder) {
        this._passwordEncoder = passwordEncoder;
    }

    private static final String BASE_DIRECTORY = "C:/var/log/applications/API/dataprocessing";

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    private static final String CSV_FILE_PATH = BASE_DIRECTORY + "/generated-data.csv";

    private static final String EXCEL_FILE_PATH = BASE_DIRECTORY + "/generated-data.xlsx";


    @GetMapping("/generate")
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

    @GetMapping("/process")
    public ResponseEntity<String> processData() {
        try {
            Path excelFilePath = Paths.get(EXCEL_FILE_PATH);

            // Check if the Excel file exists
            if (Files.exists(excelFilePath)) {
                // If Excel file exists, generate CSV from it
                generateCSVFromExcel();
            } else {
                // If Excel file doesn't exist, create CSV with headers only
                generateCSVWithHeaders();
            }
            return ResponseEntity.ok("Data processed successfully. CSV file saved at: " + CSV_FILE_PATH);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing data: " + e.getMessage());
        }
    }

    @GetMapping("/saveToDb")
    public ResponseEntity<String> uploadToDatabase() {
        try {
            List<User> users = parseXLSXFile(EXCEL_FILE_PATH);
            userRepository.saveAll(users);  // Save all users to the database
            return ResponseEntity.ok("Data uploaded to the database successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading data to DB: " + e.getMessage());
        }
    }


    /**
     * Parse the CSV file and convert the records into User entities.
     *
     * @param filePath The path to the CSV file.
     * @return A list of User entities to be saved to the database.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    private List<User> parseXLSXFile(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        try (FileInputStream excelFile = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            // Read the header row
            Row headerRow = rowIterator.next();
            Map<String, Integer> headerMap = new HashMap<>();


            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                headerMap.put(headerRow.getCell(i).getStringCellValue().trim(), i);
            }


            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();


                Long studentId = parseLong(getCellValueAsString(row.getCell(headerMap.get("Student ID")))); // Handle Student ID
                String firstName = getCellValueAsString(row.getCell(headerMap.get("First Name"))); // Handle First Name
                String lastName = getCellValueAsString(row.getCell(headerMap.get("Last Name"))); // Handle Last Name
                LocalDate dob = LocalDate.parse(getCellValueAsString(row.getCell(headerMap.get("DOB")))); // Handle DOB
                String studentClass = getCellValueAsString(row.getCell(headerMap.get("Class"))); // Handle Class
                int score = parseInteger(getCellValueAsString(row.getCell(headerMap.get("Score")))) + 5; // Handle Score (with validation)
                String photoPath = getCellValueAsString(row.getCell(headerMap.get("PhotoPath"))); // Handle PhotoPath
                int status = parseInteger(getCellValueAsString(row.getCell(headerMap.get("Status")))); // Handle Status

                User user = new User(null, firstName, "seedPassword", lastName, dob, studentClass, score, photoPath, status, new HashSet<>());
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Utility method to safely parse Long values (for Student ID).
     */
    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null; // Return null if it's not a valid number
        }
    }

    /**
     * Utility method to safely parse Integer values (for Score).
     */
    private Integer parseInteger(String value) {
        if ("N/A".equals(value)) {
            return 0; // If the score is "N/A", we set it to 0 or handle as needed
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // Return 0 for invalid scores, or you can handle as you prefer
        }
    }


    /**
     * Generates a CSV file from the existing Excel file.
     */
    private void generateCSVFromExcel() throws Exception {
        try (FileInputStream excelFile = new FileInputStream(EXCEL_FILE_PATH); BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Write header to CSV
            Row headerRow = rowIterator.next();
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                writer.write(getCellValueAsString(headerRow.getCell(i)));
                if (i < headerRow.getPhysicalNumberOfCells() - 1) writer.write(",");
            }
            writer.newLine();


            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                    String cellValue = getCellValueAsString(row.getCell(i));


                    if (i == 5) {
                        try {

                            double score = Double.parseDouble(cellValue);
                            score += 10;
                            cellValue = String.valueOf((int) score);
                        } catch (NumberFormatException e) {

                            logger.warn("Invalid score format for student in row " + row.getRowNum() + ", skipping update.");
                        }
                    }

                    writer.write(cellValue);
                    if (i < row.getPhysicalNumberOfCells() - 1) writer.write(",");
                }
                writer.newLine();
            }

            workbook.close();
        }
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();

                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    /**
     * Generates a CSV file with just the headers.
     */
    private void generateCSVWithHeaders() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header only
            writer.write("Student ID,First Name,Last Name,DOB,Class,Score,Status,PhotoPath");
            writer.newLine();
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
            Faker faker = new Faker();
            Random random = new Random();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Set<Long> studentIds = new HashSet<>();

            // Populate data rows
            for (int i = 0; i < records; i++) {
                long studentId = (i + 1);

                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(studentId);
                dataRow.createCell(1).setCellValue(generateValidName(faker.name().firstName()));
                dataRow.createCell(2).setCellValue(generateValidName(faker.name().lastName()));
                dataRow.createCell(3).setCellValue(generateRandomDOB(dateFormatter));
                dataRow.createCell(4).setCellValue("Class" + (random.nextInt(5) + 1));
                dataRow.createCell(5).setCellValue(random.nextInt(26) + 55);
                dataRow.createCell(6).setCellValue(random.nextInt(2));
                dataRow.createCell(7).setCellValue("N/A");
            }

            // Write workbook to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        }
    }

    /**
     * Generate a random date of birth (DOB) in the format yyyy-MM-dd.
     *
     * @param dateFormatter DateTimeFormatter to format the DOB.
     * @return A randomly generated DOB.
     */
    private String generateRandomDOB(DateTimeFormatter dateFormatter) {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2010, 12, 31);
        long randomDay = new Random().nextLong(startDate.toEpochDay(), endDate.toEpochDay());
        return LocalDate.ofEpochDay(randomDay).format(dateFormatter);
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
        return new Random().ints('a', 'z' + 1).limit(length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    /**
     * Generate a valid name within the 3 to 8 character length constraint.
     * Ensures that the name length is between 3 and 8 characters.
     *
     * @param name The name to validate.
     * @return A name with a length between 3 and 8 characters.
     */
    private String generateValidName(String name) {

        if (name.length() < 3) {
            return name + "a";
        } else if (name.length() > 8) {
            return name.substring(0, 8);
        }
        return name;  // Return as is if valid length
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
