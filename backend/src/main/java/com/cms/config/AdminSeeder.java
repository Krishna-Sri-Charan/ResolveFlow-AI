package com.cms.config;

import com.cms.model.Role;
import com.cms.model.User;
import com.cms.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (
                userRepository.findByEmail(
                        adminEmail
                ).isPresent()
        ) {

            return;
        }

        User admin = User.builder()
        		.name(adminName)
        		.email(adminEmail)
        		.password(passwordEncoder.encode(adminPassword))
        		.role(Role.ADMIN)
        		.build();

        userRepository.save(admin);

        System.out.println(
                "Default admin account created."
        );
    }
}