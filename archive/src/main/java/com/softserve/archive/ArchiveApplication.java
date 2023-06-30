package com.softserve.archive;

import com.softserve.amqp.config.archive.ArchiveMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class ArchiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchiveApplication.class, args);
    }
}
