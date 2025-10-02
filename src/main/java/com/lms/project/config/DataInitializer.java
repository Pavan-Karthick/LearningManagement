package com.lms.project.config;

import com.lms.project.model.AppUser;
import com.lms.project.model.Role;
import com.lms.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create admin user
            AppUser adminUser = new AppUser();
            adminUser.setFullName("Admin User");
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(Role.ADMIN);

            userRepository.save(adminUser);
            System.out.println("Admin user created: username='admin', password='admin123'");
        }
    }
}
