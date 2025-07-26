package com.kfleetguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KscopeApplication {
    public static void main(String[] args) {
        SpringApplication.run(KscopeApplication.class, args);
    }
} 