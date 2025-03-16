package com.ouchin.ourikat.entity.seeder;

import com.ouchin.ourikat.entity.Admin;
import com.ouchin.ourikat.enums.Role;
import com.ouchin.ourikat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            // Create a new Admin instance
            Admin admin = new Admin();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Encode the password
            admin.setRole(Role.ADMIN); // Set the role to ADMIN
            admin.setVerified(true); // Mark the admin as verified

            // Save the admin to the database
            userRepository.save(admin);

            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}