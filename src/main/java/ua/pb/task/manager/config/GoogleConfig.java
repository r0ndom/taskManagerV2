package ua.pb.task.manager.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.plus.PlusScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ua.pb.task.manager.service.AuthService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Configuration
@PropertySource("classpath:auth.properties")
public class GoogleConfig {

    @Value("${spreadsheets.url}")
    private String SPREADSHEETS_URL;

    @Bean
    public HttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public MemoryDataStoreFactory memoryDataStoreFactory() {
        return MemoryDataStoreFactory.getDefaultInstance();
    }

    @Bean
    public JsonFactory jsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() throws IOException, GeneralSecurityException {
        List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, PlusScopes.USERINFO_EMAIL, SPREADSHEETS_URL, GmailScopes.MAIL_GOOGLE_COM);

        InputStream in = AuthService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory(), new InputStreamReader(in));

        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport(), jsonFactory(), clientSecrets, SCOPES)
                .setDataStoreFactory(memoryDataStoreFactory())
                .setAccessType("offline")
                .build();
    }
}
