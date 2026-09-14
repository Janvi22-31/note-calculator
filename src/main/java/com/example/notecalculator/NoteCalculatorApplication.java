package com.example.notecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Note Calculator Spring Boot application.
 * Running this class starts an embedded web server (Tomcat)
 * on port 8080 and exposes our REST APIs.
 */
@SpringBootApplication
public class NoteCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteCalculatorApplication.class, args);
    }
}
