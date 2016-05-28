package is.arionbanki.arionfintechdemo;

import is.arionbanki.arionapi.oauth2.interfaces.ApiSecretsProvider;
import is.arionbanki.arionapi.oauth2.providers.ClientApiSecret;

/**
 * Created by laufd_000 on 22.05.2016.
 */
public class ApiConfig implements ApiSecretsProvider {
    
    @Override
    public ClientApiSecret loadSecrets() {
        Secrets secrets = new Secrets();
        return new ClientApiSecret.Builder()
                .setClientId(secrets.getClientId())
                .setClientSecret(secrets.getClientSecret())
                .setSubscriptionKey(secrets.getSubscriptionKey())
                .setTokenServerUrl(Constants.TOKEN_SERVER)
                .setAuthorizationServerUrl(Constants.AUTHORIZATION_SERVER)
                .setRedirectUri(Constants.REDIRECT_URI)
                .setApiBaseUri(Constants.API_URI)
                .addScopesCsv(Constants.API_SCOPES_CSV)
                .build();
    }

    private static final class Secrets {
        public String getSubscriptionKey() {
            return "0729b067ccb145718fc3f41dba2af355";
//            throw new RuntimeException("Enter your subscription key");
        }

        public String getClientId() {
            return "FinTechDemoClientAndroid";
//            throw new RuntimeException("Enter your client id");
        }

        public String getClientSecret() {
            return "afb1a872-e73a-451e-8ff0-12e7272b2a28";
//            throw new RuntimeException("Enter your client secret");
        }
    }

    private static final class Constants {
        private static final String API_URI = "https://arionapi-sandbox.azure-api.net";
        private static final String TOKEN_SERVER = "https://arionapi-identityserver3-sandbox.azurewebsites.net/connect/token";
        private static final String AUTHORIZATION_SERVER = "https://arionapi-identityserver3-sandbox.azurewebsites.net/connect/authorize";

        //TODO enter your redirect uri here
        private static final String REDIRECT_URI = "oob://localhost/MyWpfClientRedirectUrl";
        private static final String API_SCOPES_CSV =  "financial";
    }
}
