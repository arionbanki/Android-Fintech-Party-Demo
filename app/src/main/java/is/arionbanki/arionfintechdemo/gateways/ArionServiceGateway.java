package is.arionbanki.arionfintechdemo.gateways;

import com.google.api.client.auth.oauth2.Credential;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import is.arionbanki.arionapi.client.ArionApiClient;
import is.arionbanki.arionapi.client.TransportException;
import is.arionbanki.arionapi.oauth2.CredentialsException;
import is.arionbanki.arionapi.oauth2.OAuthWrapper;
import is.arionbanki.arionfintechdemo.mappers.AccountMapper;
import is.arionbanki.arionfintechdemo.mappers.NationalRegistryPartyMapper;
import is.arionbanki.arionfintechdemo.models.Account;
import is.arionbanki.arionfintechdemo.models.NationalRegistryParty;

/**
 * Created by aegirt on 23.05.2016.
 */
public class ArionServiceGateway extends Gateway {

    public ArionServiceGateway(OAuthWrapper oauth, String userId) {
        super(oauth, userId);
    }

    public List<Account> getAllAccounts() throws CredentialsException, TransportException {
        Credential credential = authorize();

        String json = new ArionApiClient(getOauth())
                .credential(credential)
                .servicePath("accounts/v1/accounts")
                .executeGet()
                .parseAsString();

        return AccountMapper.toModels(json);
    }

    public List<NationalRegistryParty> searchNationalRegistryByName(String name) throws CredentialsException, TransportException {
        Credential credential = authorize();

        String json = new ArionApiClient(getOauth())
                .credential(credential)
                .servicePathFormat("nationalregistry/v1/nationalRegistryParties/%s", name)
                .executeGet()
                .parseAsString();

        return NationalRegistryPartyMapper.toModels(json);
    }

    public NationalRegistryParty searchNationalRegistryByKennitala(String kennitala) throws TransportException, CredentialsException {
        Credential credential = authorize();

        String json = new ArionApiClient(getOauth())
                .credential(credential)
                .servicePathFormat("/nationalregistry/v1/nationalRegistryParty/%s", kennitala)
                .executeGet()
                .parseAsString();

        return NationalRegistryPartyMapper.toModel(json);
    }
}
