package is.arionbanki.arionfintechdemo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import is.arionbanki.arionapi.ArionApiConstants;
import is.arionbanki.arionapi.oauth2.CredentialsException;
import is.arionbanki.arionapi.oauth2.JWTData;
import is.arionbanki.arionapi.oauth2.OAuthWrapper;

/**
 * Created by ægir on 22.05.2016.
 */
public class FinTechApplication extends Application {

    private static final String TAG = FinTechApplication.class.getSimpleName();
    private static Context context;
    private OAuthWrapper oauth;
    private ProcessTokenRequest mProcessRequest;
    private String currentUser;

    public static Context getContext() {
        return context;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        oauth = new OAuthWrapper(this, new ApiConfig());
        setCurrentUser("fintech");
    }

    public boolean hasOAuthCredential() {
        return hasOAuthCredential(getCurrentUser());
    }

    public boolean hasOAuthCredential(String user) {
        try {
            return user != null && oauth.getCredentials(user) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String beginAuthorization() {
        return oauth.buildAuthorizationUrl();
    }

    public void authorize(String code, Handler handler) {
        mProcessRequest = new ProcessTokenRequest(handler);
        mProcessRequest.execute(code);
    }

    private void logTokenInfo(TokenResponse tokenResponse) {
        try {
            String accessToken = tokenResponse.getAccessToken();
            String refreshToken = tokenResponse.getRefreshToken();
            Long expiresSeconds = tokenResponse.getExpiresInSeconds();
            String scope = tokenResponse.getScope();
            String tokenType = tokenResponse.getTokenType();

            Log.d(ArionApiConstants.TAG, "logTokenInfo() accessToken = [" + accessToken + "]");
            Log.d(ArionApiConstants.TAG, "logTokenInfo() refreshToken = [" + refreshToken + "]");
            Log.d(ArionApiConstants.TAG, "logTokenInfo() expiresSeconds = [" + expiresSeconds + "]");
            Log.d(ArionApiConstants.TAG, "logTokenInfo() scope = [" + scope + "]");
            Log.d(ArionApiConstants.TAG, "logTokenInfo() tokenType = [" + tokenType + "]");

            Set<String> keySet = tokenResponse.keySet();

            for (String key : keySet) {
                Log.d(ArionApiConstants.TAG, "logTokenInfo() keySet key = [" + key + "]");
            }
        } catch (Exception e) {
            Log.d(ArionApiConstants.TAG, e.getMessage(), e);
        }
    }

    private void logFlowInfo(String code) {
        try {

            Log.d(ArionApiConstants.TAG, "logFlowInfo() code = [" + code + "]");
            Log.d(ArionApiConstants.TAG, "logFlowInfo() auth url = [" + oauth.getAuthorizationUrl() + "]");
            Log.d(ArionApiConstants.TAG, "logFlowInfo() redirect uri = [" + oauth.getRedirectUri() + "]");

        } catch (Exception e) {
            Log.d(ArionApiConstants.TAG, e.getMessage(), e);
        }
    }

    public String getRedirectUri() {
        return oauth.getRedirectUri();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        Log.d(TAG, String.format("CurrentUser:SET called with: currentUser = [%s]", currentUser));
        this.currentUser = currentUser;
    }

    public boolean hasCurrentUser() {
        return !TextUtils.isEmpty(currentUser);
    }

    public OAuthWrapper getOAuth() {
        return oauth;
    }

    private void storeJWTData(TokenResponse tokenResponse) throws IOException {
        JWTData jwt = JWTData.parse(tokenResponse.getAccessToken());

        Map<String, String> claimsMap = new HashMap<>();

        for (String key : jwt.keySet()) {
            claimsMap.put(String.format("jwt.%s", key), jwt.getString(key));
        }
        AppData.getInstance().putAll(claimsMap);
    }

    private class ProcessTokenRequest extends AsyncTask<String, Void, Message> {

        private final String TAG = ProcessTokenRequest.class.getSimpleName();
        private final Handler handler;

        public ProcessTokenRequest(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Message doInBackground(String... params) {
            Object obj;
            int what = ArionApiConstants.MESSAGE_TOKEN_OK;

            try {
                String code = params[0];

                logFlowInfo(code);

                TokenResponse tokenResponse = oauth
                        .newTokenRequest(code)
                        .execute();

                Log.d(TAG, String.format("doInBackground() called with: params = [%s]", Arrays.toString(params)));
                Log.d(TAG, String.format("doInBackground: Response: %s: %s", tokenResponse.getClass().getName(), tokenResponse.toPrettyString()));

                logTokenInfo(tokenResponse);

                storeJWTData(tokenResponse);
                oauth.createAndStoreCredential(tokenResponse, getCurrentUser());
                obj = tokenResponse;
            } catch (TokenResponseException e) {
                Log.e(TAG, "TokenResponseException - doInBackground: FAILED", e);
                what = ArionApiConstants.MESSAGE_TOKEN_FAILED;
                obj = new CredentialsException(e);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: FAILED", e);
                e.printStackTrace();
                what = ArionApiConstants.MESSAGE_TOKEN_FAILED;
                obj = e;
            }

            return Message.obtain(handler, what, 0, 0, obj);
        }

        @Override
        protected void onPostExecute(Message message) {
            message.sendToTarget();
            mProcessRequest = null;
        }
    }
}
