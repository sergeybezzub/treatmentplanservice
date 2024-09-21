package com.aiomed.treatmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TreatmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TreatmentServiceApplication.class, args);
    }
}

