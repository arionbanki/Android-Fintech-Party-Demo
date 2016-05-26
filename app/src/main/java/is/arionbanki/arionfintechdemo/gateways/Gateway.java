package is.arionbanki.arionfintechdemo.gateways;

import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;

import java.io.IOException;

import is.arionbanki.arionapi.client.ArionApiClient;
import is.arionbanki.arionapi.client.TransportException;
import is.arionbanki.arionapi.oauth2.CredentialsException;
import is.arionbanki.arionapi.oauth2.OAuthWrapper;

/**
 * Created by aegirt on 23.05.2016.
 */
public abstract class Gateway {
    private static final String TAG = Gateway.class.getSimpleName();
    private final OAuthWrapper oauth;
    private final String userId;

    public Gateway(OAuthWrapper oauth, String userId) {
        this.oauth = oauth;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public OAuthWrapper getOauth() {
        return oauth;
    }

    protected Credential authorize() throws CredentialsException, TransportException {
        OAuthWrapper oauth = getOauth();
        Credential credential;

        try {
            credential = oauth.getCredentials(getUserId());
            Log.d(TAG, "authorize: User: "+getUserId());
        } catch (IOException e) {
            throw new CredentialsException(oauth.getAuthorizationUrl(), e);
        }

        if (credential == null) throw new CredentialsException(oauth.getAuthorizationUrl());

        if (!oauth.isValid(credential)) {
            try {
                Log.d(TAG, "authorize: REFRESHING TOKEN");
                if(!credential.refreshToken()) {
                    throw new CredentialsException(oauth.getAuthorizationUrl());
                }
            } catch (TokenResponseException tokenEx) {
                tokenEx.printStackTrace();
                throw new CredentialsException(tokenEx);
            } catch (IOException e) {
                e.printStackTrace();
                throw new TransportException(e);
            }
        }

        return credential;
    }

    protected String getApiUri() {
        return getOauth().getApiUri();
    }

    public ArionApiClient.Header getSubscriptionHeader() {
        return new ArionApiClient.Header(ArionApiClient.SUBSCRIPTION_HEADER_NAME, getOauth().getSubscriptionKey());
    }
}
