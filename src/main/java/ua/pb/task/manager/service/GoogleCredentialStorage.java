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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.model.TokenInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Service
public class GoogleCredentialStorage {

    @Value("${redirect.url}")
    private String REDIRECT_URL;

    @Autowired
    private JsonFactory JSON_FACTORY;

    @Autowired
    private HttpTransport HTTP_TRANSPORT;

    @Autowired
    @Qualifier("userScopes")
    private GoogleAuthorizationCodeFlow flow;

    //TODO thinking about to fix this method in null-case
    public Credential load(Long id) throws IOException {
        Credential credential = flow.loadCredential(String.valueOf(id));
        return isCredentialValid(credential) ? credential : null;
    }

    public Credential createAndStore(Long id, String token) throws IOException {
        return flow.createAndStoreCredential(getTokenResponse(token), String.valueOf(id));
    }

    public Credential getNewInstance(String token) throws IOException {
        return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(getSecrets())
                .build()
                .setFromTokenResponse(getTokenResponse(token));
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

    public String getAuthUrl() {
        AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URL);

        return authorizationUrl.build();
    }

    private TokenResponse getTokenResponse(String token) throws IOException {
        return flow.newTokenRequest(token).setRedirectUri(REDIRECT_URL).execute();
    }

    private boolean isCredentialValid(Credential credential) {
        return credential != null
                && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60);
    }
}
