package com.quyet.superapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class SeminarApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeminarApplication.class, args); // ✅ đúng
    }
}
