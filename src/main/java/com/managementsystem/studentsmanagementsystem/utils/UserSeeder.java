package com.managementsystem.studentsmanagementsystem.utils;

import com.managementsystem.studentsmanagementsystem.models.Role;
import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
public class UserSeeder {

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 1) { // Only seed if DB is empty
                Role adminRole = new Role(1L, "admin");
                Role userRole = new Role(2L, "user");

                List<User> users = Arrays.asList(
                        new User(null, "Lilian", "password123", "Kagure", LocalDate.of(2005, 5, 15), "Class1", 79, "/path/to/photo1.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "John", "password123", "Doe", LocalDate.of(2006, 8, 10), "Class2", 67, "/path/to/photo2.jpg", 1, new HashSet<>(List.of(adminRole))),
                        new User(null, "Jane", "password123", "Smith", LocalDate.of(2007, 1, 25), "Class3", 81, "/path/to/photo3.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Michael", "password123", "Brown", LocalDate.of(2005, 3, 14), "Class4", 73, "/path/to/photo4.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Sarah", "password123", "Wilson", LocalDate.of(2008, 11, 20), "Class5", 85, "/path/to/photo5.jpg", 1, new HashSet<>(List.of(adminRole))),
                        new User(null, "Chris", "password123", "Johnson", LocalDate.of(2005, 4, 18), "Class1", 78, "/path/to/photo6.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Emily", "password123", "Davis", LocalDate.of(2006, 6, 7), "Class2", 72, "/path/to/photo7.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Daniel", "password123", "Garcia", LocalDate.of(2007, 9, 2), "Class3", 80, "/path/to/photo8.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Sophia", "password123", "Martinez", LocalDate.of(2005, 12, 30), "Class4", 77, "/path/to/photo9.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "James", "password123", "Hernandz", LocalDate.of(2006, 2, 15), "Class5", 83, "/path/to/photo10.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Olivia", "password123", "Lopez", LocalDate.of(2008, 10, 11), "Class1", 65, "/path/to/photo11.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Ethan", "password123", "Gonzalez", LocalDate.of(2005, 7, 8), "Class2", 74, "/path/to/photo12.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Ava", "password123", "Perez", LocalDate.of(2006, 3, 21), "Class3", 79, "/path/to/photo13.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Lucas", "password123", "Miller", LocalDate.of(2007, 5, 9), "Class4", 81, "/path/to/photo14.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Charlote", "password123", "Moore", LocalDate.of(2008, 9, 16), "Class5", 69, "/path/to/photo15.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Elijah", "password123", "Taylor", LocalDate.of(2005, 4, 2), "Class1", 77, "/path/to/photo16.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Isabella", "password123", "Anderson", LocalDate.of(2006, 6, 14), "Class2", 83, "/path/to/photo17.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Matthew", "password123", "Thomas", LocalDate.of(2007, 7, 28), "Class3", 71, "/path/to/photo18.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Amelia", "password123", "Jackson", LocalDate.of(2008, 11, 6), "Class4", 76, "/path/to/photo19.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Alex", "password123", "White", LocalDate.of(2005, 10, 12), "Class5", 70, "/path/to/photo20.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Mia", "password123", "Martinez", LocalDate.of(2007, 2, 4), "Class2", 68, "/path/to/photo21.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Benja", "password123", "Evans", LocalDate.of(2006, 12, 25), "Class3", 75, "/path/to/photo22.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Harper", "password123", "Roberts", LocalDate.of(2008, 1, 18), "Class4", 82, "/path/to/photo23.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "William", "password123", "Taylor", LocalDate.of(2005, 9, 5), "Class1", 84, "/path/to/photo24.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Chloe", "password123", "Adams", LocalDate.of(2006, 7, 15), "Class2", 79, "/path/to/photo25.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Jackson", "password123", "Scott", LocalDate.of(2007, 8, 22), "Class3", 77, "/path/to/photo26.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Madeline", "password123", "Baker", LocalDate.of(2008, 3, 9), "Class4", 80, "/path/to/photo27.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Gabriel", "password123", "Nelson", LocalDate.of(2005, 11, 1), "Class5", 74, "/path/to/photo28.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Liam", "password123", "Carter", LocalDate.of(2006, 9, 17), "Class1", 70, "/path/to/photo29.jpg", 1, new HashSet<>(List.of(userRole))),
                        new User(null, "Amos", "password123", "Fisher", LocalDate.of(2007, 10, 14), "Class2", 65, "/path/to/photo30.jpg", 1, new HashSet<>(List.of(userRole)))
                );

                userRepository.saveAll(users);
                System.out.println("Database seeded with 30 user records.");
            } else {
                System.out.println("Database already has data. Skipping seeding.");
            }
        };
    }
}
