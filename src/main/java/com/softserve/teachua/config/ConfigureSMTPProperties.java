package com.softserve.teachua.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application-dev.properties")
public class ConfigureSMTPProperties {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("spring.mail.host");
        mailSender.setPort(587);
        mailSender.setUsername("spring.mail.username");
        mailSender.setPassword("spring.mail.password");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.host", "spring.mail.host");
        properties.put("mail.smtp.port", "spring.mail.port");
        properties.put("mail.smtp.auth", "spring.mail.properties.mail.smtp.auth");
        properties.put("mail.smtp.starttls.enable", "spring.mail.properties.mail.smtp.starttls.enable");
        properties.put("mail.transport.protocol", "spring.mail.properties.mail.transport.protocol");

        return mailSender;
    }
}
