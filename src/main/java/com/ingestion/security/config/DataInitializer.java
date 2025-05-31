package com.ingestion.security.config;

import com.ingestion.security.model.User;
import com.ingestion.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            if (!userRepository.existsByUsername("admin")) {
                // Create admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setFullName("Admin User");
                admin.setEmail("admin@hospital.com");
                admin.setEnabled(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                
                Set<User.UserRole> adminRoles = new HashSet<>();
                adminRoles.add(User.UserRole.ROLE_ADMIN);
                admin.setRoles(adminRoles);
                
                userRepository.save(admin);
                System.out.println("Admin user created successfully");
            }
            
            // Create doctor user
            if (!userRepository.existsByUsername("doctor")) {
                User doctor = new User();
                doctor.setUsername("doctor");
                doctor.setPassword(passwordEncoder.encode("password"));
                doctor.setFullName("Dr. Sarah Wilson");
                doctor.setEmail("sarah.wilson@hospital.com");
                doctor.setEnabled(true);
                doctor.setCreatedAt(LocalDateTime.now());
                doctor.setUpdatedAt(LocalDateTime.now());
                
                Set<User.UserRole> doctorRoles = new HashSet<>();
                doctorRoles.add(User.UserRole.ROLE_DOCTOR);
                doctor.setRoles(doctorRoles);
                
                userRepository.save(doctor);
                System.out.println("Doctor user created successfully");
            }
            
            // Create nurse user
            if (!userRepository.existsByUsername("nurse")) {
                User nurse = new User();
                nurse.setUsername("nurse");
                nurse.setPassword(passwordEncoder.encode("password"));
                nurse.setFullName("Nurse Emily Davis");
                nurse.setEmail("emily.davis@hospital.com");
                nurse.setEnabled(true);
                nurse.setCreatedAt(LocalDateTime.now());
                nurse.setUpdatedAt(LocalDateTime.now());
                
                Set<User.UserRole> nurseRoles = new HashSet<>();
                nurseRoles.add(User.UserRole.ROLE_NURSE);
                nurse.setRoles(nurseRoles);
                
                userRepository.save(nurse);
                System.out.println("Nurse user created successfully");
            }
            
            // Create receptionist user
            if (!userRepository.existsByUsername("reception")) {
                User receptionist = new User();
                receptionist.setUsername("reception");
                receptionist.setPassword(passwordEncoder.encode("password"));
                receptionist.setFullName("Receptionist Mark Taylor");
                receptionist.setEmail("mark.taylor@hospital.com");
                receptionist.setEnabled(true);
                receptionist.setCreatedAt(LocalDateTime.now());
                receptionist.setUpdatedAt(LocalDateTime.now());
                
                Set<User.UserRole> receptionistRoles = new HashSet<>();
                receptionistRoles.add(User.UserRole.ROLE_RECEPTIONIST);
                receptionist.setRoles(receptionistRoles);
                
                userRepository.save(receptionist);
                System.out.println("Receptionist user created successfully");
            }
            
            // Create lab technician user
            if (!userRepository.existsByUsername("lab")) {
                User labTech = new User();
                labTech.setUsername("lab");
                labTech.setPassword(passwordEncoder.encode("password"));
                labTech.setFullName("Lab Tech Alex Johnson");
                labTech.setEmail("alex.johnson@hospital.com");
                labTech.setEnabled(true);
                labTech.setCreatedAt(LocalDateTime.now());
                labTech.setUpdatedAt(LocalDateTime.now());
                
                Set<User.UserRole> labTechRoles = new HashSet<>();
                labTechRoles.add(User.UserRole.ROLE_LAB_TECHNICIAN);
                labTech.setRoles(labTechRoles);
                
                userRepository.save(labTech);
                System.out.println("Lab technician user created successfully");
            }
        };
    }
}