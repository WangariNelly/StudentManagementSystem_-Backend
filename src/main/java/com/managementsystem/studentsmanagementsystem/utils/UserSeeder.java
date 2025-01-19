package com.managementsystem.studentsmanagementsystem.utils;

import com.managementsystem.studentsmanagementsystem.models.Role;
import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import com.managementsystem.studentsmanagementsystem.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
public class UserSeeder {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository) {
        return args -> {

            var seedPassword = passwordEncoder.encode("password123");
            if (userRepository.count() == 0) { // Only seed if DB is empty
                Role adminRole = new Role(1L, "admin");
                Role userRole = new Role(2L, "user");

                List<User> users = Arrays.asList(
                        new User(null, "Lilian", seedPassword, "Kagure", LocalDate.of(2005, 5, 15), "Class1", 79, "/path/to/photo1.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "John", seedPassword, "Doe", LocalDate.of(2006, 8, 10), "Class2", 67, "/path/to/photo2.jpg", 1, new HashSet<>(List.of(adminRole))),
                        new User(null, "Jane", seedPassword, "Smith", LocalDate.of(2007, 1, 25), "Class3", 81, "/path/to/photo3.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Michael", seedPassword, "Brown", LocalDate.of(2005, 3, 14), "Class4", 73, "/path/to/photo4.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Sarah", seedPassword, "Wilson", LocalDate.of(2008, 11, 20), "Class5", 85, "/path/to/photo5.jpg", 1, new HashSet<>(List.of(adminRole))),
                        new User(null, "Chris", seedPassword, "Johnson", LocalDate.of(2005, 4, 18), "Class1", 78, "/path/to/photo6.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Emily", seedPassword, "Davis", LocalDate.of(2006, 6, 7), "Class2", 72, "/path/to/photo7.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Daniel", seedPassword, "Garcia", LocalDate.of(2007, 9, 2), "Class3", 80, "/path/to/photo8.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Sophia", seedPassword, "Martinez", LocalDate.of(2005, 12, 30), "Class4", 77, "/path/to/photo9.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "James", seedPassword, "Hernandz", LocalDate.of(2006, 2, 15), "Class5", 83, "/path/to/photo10.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Olivia", seedPassword, "Lopez", LocalDate.of(2008, 10, 11), "Class1", 65, "/path/to/photo11.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Ethan", seedPassword, "Gonzalez", LocalDate.of(2005, 7, 8), "Class2", 74, "/path/to/photo12.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Ava", seedPassword, "Perez", LocalDate.of(2006, 3, 21), "Class3", 79, "/path/to/photo13.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Lucas", seedPassword, "Miller", LocalDate.of(2007, 5, 9), "Class4", 81, "/path/to/photo14.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Charlote", seedPassword, "Moore", LocalDate.of(2008, 9, 16), "Class5", 69, "/path/to/photo15.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Elijah", seedPassword, "Taylor", LocalDate.of(2005, 4, 2), "Class1", 77, "/path/to/photo16.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Isabella", seedPassword, "Anderson", LocalDate.of(2006, 6, 14), "Class2", 83, "/path/to/photo17.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Matthew", seedPassword, "Thomas", LocalDate.of(2007, 7, 28), "Class3", 71, "/path/to/photo18.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Amelia", seedPassword, "Jackson", LocalDate.of(2008, 11, 6), "Class4", 76, "/path/to/photo19.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Alex", seedPassword, "White", LocalDate.of(2005, 10, 12), "Class5", 70, "/path/to/photo20.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Mia", seedPassword, "Martinez", LocalDate.of(2007, 2, 4), "Class2", 68, "/path/to/photo21.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Benja", seedPassword, "Evans", LocalDate.of(2006, 12, 25), "Class3", 75, "/path/to/photo22.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Harper", seedPassword, "Roberts", LocalDate.of(2008, 1, 18), "Class4", 82, "/path/to/photo23.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "William", seedPassword, "Taylor", LocalDate.of(2005, 9, 5), "Class1", 84, "/path/to/photo24.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Chloe", seedPassword, "Adams", LocalDate.of(2006, 7, 15), "Class2", 79, "/path/to/photo25.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Jackson", seedPassword, "Scott", LocalDate.of(2007, 8, 22), "Class3", 77, "/path/to/photo26.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Madeline", seedPassword, "Baker", LocalDate.of(2008, 3, 9), "Class4", 80, "/path/to/photo27.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Gabriel", seedPassword, "Nelson", LocalDate.of(2005, 11, 1), "Class5", 74, "/path/to/photo28.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Liam", seedPassword, "Carter", LocalDate.of(2006, 9, 17), "Class1", 70, "/path/to/photo29.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Amos", seedPassword, "Fisher", LocalDate.of(2007, 10, 14), "Class2", 65, "/path/to/photo30.jpg", 1, new HashSet<>(List.of(userRole)))
                );

                userRepository.saveAll(users);
                System.out.println("Database seeded with 30 user records.");
            } else {
                System.out.println("Database already has data. Skipping seeding.");
            }
        };
    }
}
