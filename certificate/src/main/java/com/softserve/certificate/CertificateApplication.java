package com.softserve.certificate;

import com.softserve.amqp.config.archive.ArchiveMQProducerConfig;
import com.softserve.commons.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import({ArchiveMQProducerConfig.class, FeignConfig.class})
public class CertificateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }
}
