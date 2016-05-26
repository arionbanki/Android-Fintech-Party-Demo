package is.arionbanki.arionfintechdemo.mappers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import is.arionbanki.arionfintechdemo.models.Account;

/**
 * Created by aegirt on 23.05.2016.
 */
public class AccountMapper {

    private static final String TAG = AccountMapper.class.getSimpleName();

    public static List<Account> toModels(String json) {
        ArrayList<Account> results = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray jsonItems = root.getJSONArray("account");
            for (int i = 0; i < jsonItems.length(); i++) {
                results.add(toModel(jsonItems.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("BalanceItems", e);
        }
        return results;
    }

    private static Account toModel(JSONObject jsonObject) {
        String accountId = jsonObject.optString("accountID", null);
        String accountOwnerID = jsonObject.optString("accountOwnerID", null);
        String customAccountName = jsonObject.optString("customAccountName", null);
        double balance = jsonObject.optDouble("balance", Double.NaN);
        //TODO: add the following properties to Account model
//        String status = jsonObject.optString("status", null);
//        double overdraft = jsonObject.optDouble("overdraft", Double.NaN);
//        double availableAmount = jsonObject.optDouble("availableAmount", Double.NaN);
//        double totalAmountWaiting = jsonObject.optDouble("totalAmountWaiting", Double.NaN);

        return new Account()
                .setAccountId(accountId)
                .setCustomName(customAccountName)
                .setAccountId(accountId)
                .setBalance(balance);
    }
}
