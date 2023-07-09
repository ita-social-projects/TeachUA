package com.softserve.club;

import com.softserve.amqp.config.archive.ArchiveMQProducerConfig;
import com.softserve.commons.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ArchiveMQProducerConfig.class, FeignConfig.class})
public class ClubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClubApplication.class, args);
    }
}
