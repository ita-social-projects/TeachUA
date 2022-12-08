package com.softserve.teachua.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
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

    @Value("${application.api.credentials.gmail.clientId}")
    private String googleApiClientId;

    @Value("${application.api.credentials.gmail.clientSecret}")
    private String googleApiClientSecret;

    @Value("${application.api.credentials.gmail.refreshToken}")
    private String googleApiRefreshToken;

    @Value("${application.api.credentials.service-account.clientEmail}")
    private String serviceAccountEmail;

    @Value("${application.api.credentials.service-account.privateKey}")
    private String serviceAccountPrivateKey;

    private final String APPLICATION_NAME = "TeachUA ";

    @Bean
    public UserCredentials gmailCredentials() {
        return UserCredentials.newBuilder()
            .setClientId(googleApiClientId)
            .setClientSecret(googleApiClientSecret)
            .setRefreshToken(googleApiRefreshToken)
            .build();
    }

    @Bean
    public ServiceAccountCredentials serviceAccountCredentials() {
        try {
            serviceAccountPrivateKey = serviceAccountPrivateKey.replace("\\n", "\n");
            return ServiceAccountCredentials.newBuilder()
                .setClientEmail(serviceAccountEmail)
                .setPrivateKeyString(serviceAccountPrivateKey)
                .setScopes(FormsScopes.all())
                .build();
        } catch (IOException e) {
            log.error("Exception during creating ServiceAccountCredentials, occurred by {} {}",
                e.getClass(), e.getMessage());
            return ServiceAccountCredentials.newBuilder().build();
        }
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
    public Gmail gmail(HttpTransport httpTransport, JsonFactory jsonFactory, UserCredentials credentials) {
        return new Gmail.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Gmail")
            .build();
    }

    @Bean
    public Forms forms(HttpTransport httpTransport, JsonFactory jsonFactory, ServiceAccountCredentials credentials) {
        return new Forms.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Forms").build();
    }

    @Bean
    public Drive drive(HttpTransport httpTransport, JsonFactory jsonFactory, ServiceAccountCredentials credentials) {
        return new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Drive").build();
    }
}
