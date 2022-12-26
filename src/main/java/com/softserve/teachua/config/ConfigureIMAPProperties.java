package com.softserve.teachua.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Class get settings to IMAP.
 */

@Configuration
@PropertySource(value = "classpath:application-dev.properties")
public class ConfigureIMAPProperties {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public Session imapSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "imap.gmail.com");
        return Session.getInstance(properties,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
    }
}
