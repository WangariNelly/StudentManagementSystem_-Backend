package com.managementsystem.studentsmanagementsystem.models;

import com.managementsystem.studentsmanagementsystem.validations.BirthValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false, length = 8)
    @Size(min = 3, max = 8 , message = "First name must have 3 to 8 characters")
    private String firstName;

    private String password;

    @Column(nullable = false, length = 8)
    @Size(min = 3, max = 8 , message = "Last name must have 3 to 8 characters")
    private String lastName;

    @NotNull(message = "Date of Birth required!",groups = Default.class)
    @PastOrPresent(message = "Date of Birth must be a valid past or present date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @BirthValidator
    private LocalDate dob;

    @NotBlank(message = "Class name can not be empty!", groups = Default.class)
    @Pattern( regexp = "Class[1-5]", message = "Class Name must be one of the options: Class1, Class2, Class3, Class4, Class5 ")
    private String className;

    @Column(nullable = false)
    @Min(value = 55, message = "Score must be at least 55.",groups = Default.class)
    @Max( value = 85, message = "Score must not exceed 85")
    private int score = 55;

    @Column(nullable = false)
    private int status =  1;

    @Column(nullable = false)
    private String photoPath = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<Role> roles = new HashSet<>();

    public User(Long studentId, String firstName, String password, String lastName, LocalDate dob, String className, int score, String photoPath, int status, Set<Role> roles) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.dob = dob;
        this.className = className;
        this.score = score;
        this.photoPath = photoPath;
        this.status = status;
        this.roles = roles;
    }

    public User() {
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
