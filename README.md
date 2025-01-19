# Student Management System

## Overview

The **Student Management System** is a full-stack application built with **Spring Boot (Java 17)** for the backend and **Angular 18** for the frontend. This application is designed to manage student data, provide robust data processing features, and generate comprehensive reports. It uses **JWT-based authentication** for secure API access and integrates with **Microsoft SQL Server** for data storage.

## Features

### Frontend (Angular)

- **Login Page**  
  Authenticate users and provide a JWT token upon successful login.

- **Dashboard**  
  Displays the total number of students in the database.

- **Menu Items**
    - **Data Generation**: Generate student data and save it in an Excel file.
    - **Data Processing**: Read generated Excel files, process them, and save results as CSV files.
    - **File Upload**: Upload Excel files and store data in the database.
    - **Student Management**: Perform CRUD operations for student data,View, filter, and export student reports with pagination.


### Backend (Spring Boot)

- **Secure API Endpoints**  
  All endpoints (except login) are secured using **JWT-based authentication**.

- **Data Generation**  
  Generates student records and saves them in an Excel file.  
  **File is stored in**:
    - Windows: `C:\var\log\applications\API\dataprocessing\<excel-file-name>`
    - Linux: `/var/log/applications/API/dataprocessing/<excel-file-name>`

- **Data Processing**  
  Reads generated Excel files and saves the data in a CSV file. Adds 10 to the student scores before saving the CSV.

- **Data Upload**  
  Reads Excel files and stores the data in the database. Adds 5 to the student scores before saving in the database.

- **Student Management**  
  View, edit, delete, and upload photos for students.  
  Supports soft deletes by setting status to `0` (inactive).  
  **Uploaded photos are stored in**:
    - Windows: `C:\var\log\applications\API\StudentPhotos\<studentId-filename>`
    - Linux: `/var/log/applications/API/StudentPhotos/<studentId-filename>`

- **Student Reports**
    - Pagination and filtering options:
        - Search by `studentId`
        - Filter by `class` (dropdown)
        - Filter by `date of birth` range (start date and end date)
    - Export reports to Excel files.

## System Requirements

### Backend
- **Java Version**: 17 or higher
- **Spring Boot Version**: Compatible with Java 17
- **Database**: Microsoft SQL Server
- **Other Dependencies**:
    - Spring Security
    - Spring Data JPA
    - Springdoc OpenAPI for Swagger

### Frontend
- **Angular Version**: 18 or higher
- **Node.js Version**: 16 or higher
- **Package Manager**: npm or yarn

## How to Run

### 1. Clone the Repository


git clone <https://github.com/WangariNelly/StudentManagementSystem_-Backend>

## 2. Backend Setup

### Build application
- mvn clean install


### Database Configurations
spring.datasource.url=jdbc:sqlserver://<your-server>;databaseName=Student_db_prod;encrypt=false;trustServerCertificate=true;
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

### Backend Server 
- mvn spring-boot:run

## Frontend Setup
npm  install

### Development Server
ng serve

## Access the Application
  - **Frontend: http://localhost:4200**
  - **Backend API Documentation (Swagger): http://localhost:8080/swagger-ui/index.html**


  - ### Endpoints Overview
**Authentication**
- **POST /auth/login:** Authenticate user and generate JWT token.

**Data Generation**
- **GET /api/data/generate?records=<number>:** Generate an Excel file with student data.

**Data Processing**
- **POST /api/data/process: Process an Excel file and save it as CSV.**

**Data Upload**
- **POST /api/data/upload:** Upload and process an Excel file into the database.

**Student Management**

- **GET /api/students:** List all students.
- **PUT /api/students/{id}:** Update student data.
- **DELETE /api/students/{id}:** Soft delete a student.

## Submission Details
- Demonstration video: Demo Link