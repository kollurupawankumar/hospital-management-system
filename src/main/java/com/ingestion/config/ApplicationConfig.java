package com.ingestion.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.SpringApplication;

/**
 * Application configuration class.
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * This configuration is needed during the transition period
     * while we're migrating from the old package structure to the new one.
     * It allows bean definition overriding, which is needed when we have
     * duplicate bean definitions during the migration.
     */
    @Bean
    public org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations webMvcRegistrations() {
        return new org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations() {};
    }
}