package com.softserve.teachua.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Class get settings for Google API services.
 */
@Slf4j
@Configuration
@PropertySource(value = "classpath:application.yaml")
public class GoogleApiConfig {

    @Value("${application.api.google.credentials.clientId}")
    private String googleApiClientId;

    @Value("${application.api.google.credentials.clientSecret}")
    private String googleApiClientSecret;

    @Value("${application.api.google.credentials.refreshToken}")
    private String googleApiRefreshToken;

    private final String APPLICATION_NAME = "TeachUA";

    @Bean
    public GoogleCredentials googleCredentials() {
        return UserCredentials.newBuilder()
            .setClientId(googleApiClientId)
            .setClientSecret(googleApiClientSecret)
            .setRefreshToken(googleApiRefreshToken)
            .build();
    }

    @Bean
    public HttpTransport httpTransport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Exception during creating GoogleNetHttpTransport, occurred by {} {}",
                e.getClass(), e.getMessage());
            return new NetHttpTransport();
        }
    }

    @Bean
    public JsonFactory jsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    public Gmail gmailService(HttpTransport httpTransport, JsonFactory jsonFactory, GoogleCredentials googleCredentials) {
        return new Gmail.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(googleCredentials))
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
}
