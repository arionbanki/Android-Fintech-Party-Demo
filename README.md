# Android-Fintech-Party-Demo
Demo biðlari Android, auðkennir og kallar á API með einfaldri virkni.
(Í vinnslu)

Til að geta sótt gögn frá Fintech þjónustunum þarf að stilla upp subscription key, client id, client secret og redirect uri. Þessar stillingar færðu með því að skrá þig á https://arionapi-sandbox.portal.azure-api.net 
<br/><br/>
Lykilinn þarf að setja inn í ApiConfig.java
<br/>
<div class="highlight highlight-source-shell"><pre>
    
public class ApiConfig implements ApiSecretsProvider {
    ...
    private static final class Secrets {
        public String getSubscriptionKey() {
            throw new RuntimeException("Enter your subscription key");
        }

        public String getClientId() {
            throw new RuntimeException("Enter your client id");
        }

        public String getClientSecret() {
            throw new RuntimeException("Enter your client secret");
        }
    }
    
    private static final class Constants {
        ...
        private static final String REDIRECT_URI = "Enter your redirect uri";
        ...
    }
}
</pre></div>
