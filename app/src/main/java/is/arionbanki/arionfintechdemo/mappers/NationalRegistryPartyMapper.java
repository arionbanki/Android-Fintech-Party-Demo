package is.arionbanki.arionfintechdemo.mappers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import is.arionbanki.arionfintechdemo.models.NationalRegistryParty;

/**
 * Created by aegirt on 25.05.2016.
 */
public class NationalRegistryPartyMapper {
    private static final String TAG = NationalRegistryPartyMapper.class.getSimpleName();

    public static List<NationalRegistryParty> toModels(String json) {
        ArrayList<NationalRegistryParty> results = new ArrayList<>();
        try {
            JSONArray jsonItems = new JSONArray(json);
            for (int i = 0; i < jsonItems.length(); i++) {
                results.add(toModel(jsonItems.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return results;
    }

    public static NationalRegistryParty toModel(String json) {
        try {
            return toModel(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static NationalRegistryParty toModel(JSONObject jsonObject) {
        String city = jsonObject.optString("City", null);
        int fate = jsonObject.optInt("Fate", 0);
        String fullName = jsonObject.optString("FullName", null);
        String home = jsonObject.optString("Home", null);
        String kennitala = jsonObject.optString("Kennitala", null);
        String postalCode = jsonObject.optString("PostalCode", null);

        return new NationalRegistryParty()
                .setCity(city)
                .setFate(fate)
                .setFullName(fullName)
                .setHome(home)
                .setKennitala(kennitala)
                .setPostalCode(postalCode);
    }
}
