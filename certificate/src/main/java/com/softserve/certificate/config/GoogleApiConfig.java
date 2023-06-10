package com.softserve.certificate.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class get settings for Google API services.
 */
@Slf4j
@Configuration
public class GoogleApiConfig {
    @Value("${application.api.credentials.service-account.clientEmail}")
    private String serviceAccountEmail;

    @Value("${application.api.credentials.service-account.privateKey}")
    private String serviceAccountPrivateKey;

    private static final String APPLICATION_NAME = "TeachUA ";

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
