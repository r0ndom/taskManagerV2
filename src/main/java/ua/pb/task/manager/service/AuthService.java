package ua.pb.task.manager.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.dao.UserDao;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mednikov on 09.03.2016.
 */
@Service
public class AuthService {

    @Autowired
    private TokenHandler handler;

    private static final String APPLICATION_NAME = "taskmanager-1238";

    private static final String REDIRECT_URL = "http://localhost:8080/auth";

    private static final String SPREADSHEETS_URL = "https://spreadsheets.google.com/feeds/";

    private static final String PERSONAL_INFO_URL = "https://www.googleapis.com/auth/userinfo.profile";

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static JsonFactory JSON_FACTORY;

    private static HttpTransport HTTP_TRANSPORT;

    private static List<String> SCOPES;

    @PostConstruct
    public void init() {
        try {
            File DATA_STORE_DIR = new File(System.getProperty("user.home"),
                    ".credentials/drive-java-quickstart.json");
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            JSON_FACTORY = JacksonFactory.getDefaultInstance();
            SCOPES = Arrays.asList(DriveScopes.DRIVE, SPREADSHEETS_URL, PERSONAL_INFO_URL);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private Credential authorize(Long id) throws IOException {

        InputStream in = AuthService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = flow.loadCredential(String.valueOf(id));
        if (credential != null
                && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
            return credential;
        }

        AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URL);
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    System.out.println("Attempting to open that address in the default browser now...");
                    desktop.browse(URI.create(authorizationUrl.build()));
                }
            }
        } catch (IOException | InternalError e) {
            e.printStackTrace();
        }
        String code = handler.getToken();
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URL).execute();
        return flow.createAndStoreCredential(response, String.valueOf(id));
    }

    private Drive getDriveService(Credential credential) throws IOException {
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
