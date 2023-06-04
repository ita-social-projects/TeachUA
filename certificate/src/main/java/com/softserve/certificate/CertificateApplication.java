package com.softserve.certificate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CertificateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }
}
