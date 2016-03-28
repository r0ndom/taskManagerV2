package ua.pb.task.manager.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.util.TokenHandler;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Service
public class GoogleCredentialStorage {

    @Autowired
    private TokenHandler handler;
    @Autowired
    private HttpServletResponse response;

    @Value("${redirect.url}")
    private String REDIRECT_URL;

    @Autowired
    private JsonFactory JSON_FACTORY;

    @Autowired
    private HttpTransport HTTP_TRANSPORT;

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    public Credential load(Long id) throws IOException {
        Credential credential = flow.loadCredential(String.valueOf(id));
        return isCredentialValid(credential) ? credential : createAndStore(id);
    }

    public Credential createAndStore(Long id) throws IOException {
        return flow.createAndStoreCredential(getTokenResponse(), String.valueOf(id));
    }

    public Credential getNewInstance() throws IOException {
        return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(getSecrets())
                .build()
                .setFromTokenResponse(getTokenResponse());
    }


    //FIXME need to find another solution
    public Credential store(Long id, Credential credential) throws IOException {
        if (flow.getCredentialStore() != null) {
            flow.getCredentialStore().store(String.valueOf(id), credential);
        }
        if (flow.getCredentialDataStore() != null) {
            flow.getCredentialDataStore().set(String.valueOf(id), new StoredCredential(credential));
        }
        return credential;
    }

    private GoogleClientSecrets getSecrets() throws IOException {
        InputStream in = AuthService.class.getResourceAsStream("/client_secret.json");
        return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    }

    private TokenResponse getTokenResponse() throws IOException {
        AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URL);

        //response.sendRedirect(authorizationUrl.build());

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    System.out.println("Attempting to open that address in the default browser now...");
                    desktop.browse(URI.create(authorizationUrl.build()));
                }
            }
        } catch (IOException e) {
        } catch (InternalError e) {

        }

        String code = handler.getToken();
        return flow.newTokenRequest(code).setRedirectUri(REDIRECT_URL).execute();
    }

    private boolean isCredentialValid(Credential credential) {
        return credential != null
                && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60);
    }
}
