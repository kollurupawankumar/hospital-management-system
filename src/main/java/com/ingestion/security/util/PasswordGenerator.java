package com.ingestion.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Password matches: " + encoder.matches(rawPassword, encodedPassword));
        
        // Check if our hardcoded password in data.sql is valid
        String storedHash = "$2a$10$rAje4Gy7vnH0z0yBR9WZFer6MxW0gbdc2zougRy0uY1JgPxpGUYUO";
        System.out.println("Stored hash matches 'password': " + encoder.matches(rawPassword, storedHash));
    }
}