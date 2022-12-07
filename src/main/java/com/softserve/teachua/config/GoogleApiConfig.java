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

    @Value("${application.api.google.credentials.clientId}")
    private String googleApiClientId;

    @Value("${application.api.google.credentials.clientSecret}")
    private String googleApiClientSecret;

    @Value("${application.api.google.credentials.refreshToken}")
    private String googleApiRefreshToken;

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
            return ServiceAccountCredentials.newBuilder()
                .setClientEmail("speak-ukrainian@speak-ukrainian-362008.iam.gserviceaccount.com")
                .setPrivateKeyString("-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCLF3l+rUwf1STb\nISicYDMQL4RwIc49ghH5UuN065IJwYbw8jF2VDZCa+dKiRDK7igKJwRtlxtd08zi\nCbX/anLTGPimSjOJPcV0h+1g1xrmrjQEDjq7PU6QNbGSMJW/DMk3joHh2iX1B6ph\ns0k9zltDQSmBmviY+fw2VtnovU2aMxvkdHX/n0t+AJcAkjRIL1E+lk5fE1+TNqbJ\nMCpDMQWbNi+9WOurnCs6S6LzF/T0UThhwHCvIU7Uvj9R7/h6L+Yo5JsvWBfqJdv9\nKagulg9fK8ufTAa2aKA4TOFA2P10/xFfHGf2laGsnkvyYMa9m6sRBwQb9r6Wg5Xr\nyxdFhbQlAgMBAAECggEAAkn4G2ARtSJmUV7Z0rz4iicmCExeHwABeLdm+9kWlaeE\nJ3ns23QmUnCBJWZdCuHhcc1TktQeRKyEHiInxxTC3/t0uBPXhkYodjL4cpQHos0y\nGPD9AXDQVqozimLXTAYTV6E2PPfAkSg3oxT2uxgmw1QSsUnSFn5iqqhCPMTEvqlb\ncalryfR/DKmhnlKBLVjwlycnvZMTbi4E0hmsUYWd4bsnS4xAnSE99+2c0JTJa28I\na/INptaah64FsuqhlZkJRVHuN9AhfCM6FVRbbwg16oRqakU+FhI9lhf3Fj3ibtdg\nfqD9PxhBrRfAoU6aFUMgXUuWn82hLzpeuY6HFNZf8QKBgQC+Le9VIr0yTvXJwQgJ\nb/z0iJsv+FUT594y0P4I1f4I9wcIpkLWTQrY5/14L8zNX0Tn0U93o8vgX2dpTuub\npRfYo3xXB1+VKHvIltmlcM4h2Gw7Ae2HXV8tvkkbWc6gLWZekRj56+Ytxp16ruB2\nmIYPRqJ9xId0ZjEXIGp9KX1x6QKBgQC7Ox8SiBNZ1BZ8FG2u+WZ6mNherFXaYnxp\n+MprrRDyluIe+M+MFKmCd28adQ6gOxjt8Ye8qQDXbQTaxg3bacaMbYEP4K2NBKuQ\n9AjbXWA1/ODP8oveVrRQVc3ZFIDE1i4o5K+M4SU9sifPtOhORzcqd3G3vVLPB3tz\nxjbK656u3QKBgGqWnivuI3Hd1V31ZUa80F5RbgDtwuk8LEn43lgfb5igsv2DCbTI\nS0sUgvhoefWk7p6qrkpUsECZ2ACqrdx3JC1UO60fQc/8d5wOB//0gGNxw/8ybmlO\nyLh/jMPairc1mslns0LnlwOS7NJEzyNJCuHm6c1pZ62yVNMsZOKvbVJ5AoGAL4KD\nEfrBnku5IOc2Je5XWMisLungJzhShbmB8+zIniY3XtCYKd2cXeTcpoQJoBMe8lIV\ntYPVGNUrJDCZl5GxtjSbSWIj5+LFflTOYovBPDHdAidz4olx2+jKAUm4mzoWIH5X\nJZKEFrG8dHJdeG+NyhLhIlTCTJKDew9THA7KB2kCgYEAjXITcPn0yst87x8NUEmB\ntLNkgrehC8XvayvZ/VcJkxAcpfLlx5BeKXFSQ58Vqr3gfM+dqsPn1K+dwC1KhGSr\naorCd0mfeM91as72hQ0RKJsOaCH5BpgX0ogr1GKqL/cSVwmvPbcNBD6fwB4gGxR7\nTdu065ULlyiXwQ1kyeoUodw=\n-----END PRIVATE KEY-----\n")
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
    public Gmail gmailService(HttpTransport httpTransport, JsonFactory jsonFactory, UserCredentials credentials) {
        return new Gmail.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Gmail")
            .build();
    }

    @Bean
    public Forms formsService(HttpTransport httpTransport, JsonFactory jsonFactory, ServiceAccountCredentials credentials) {
        return new Forms.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Forms").build();
    }

    @Bean
    public Drive driveService(HttpTransport httpTransport, JsonFactory jsonFactory, ServiceAccountCredentials credentials) {
        return new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME + "Drive").build();
    }
}
