package com.managementsystem.studentsmanagementsystem.models;

import com.managementsystem.studentsmanagementsystem.validations.BirthValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false, length = 8)
    @Size(min = 3, max = 8 , message = "First name must have 3 to 8 characters")
    private String firstName;

    @Column(nullable = false, length = 8)
    @Size(min = 3, max = 8 , message = "Last name must have 3 to 8 characters")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "Date of Birth required!")
    @BirthValidator
    private LocalDate dob;

    @Column(nullable = false)
    @NotNull(message = "Class name can not be empty!")
    @Pattern( regexp = "Class[1-5]", message = "Class Name must be one of the options: Class1, Class2, Class3, Class4, Class5 ")
    private String className;

    @Column(nullable = false)
    @Min(value = 55, message = "Score must be at least 55.")
    @Max( value = 85, message = "Score must not exceed 85")
    private int score;

    @Column(nullable = false)
    private int status =  1;

    @Column(nullable = false)
    private String photoPath = "";

}
