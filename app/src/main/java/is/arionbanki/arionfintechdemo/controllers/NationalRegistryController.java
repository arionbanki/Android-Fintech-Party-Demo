package is.arionbanki.arionfintechdemo.controllers;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import is.arionbanki.arionapi.utils.AsyncTaskResult;
import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.gateways.ArionServiceGateway;
import is.arionbanki.arionfintechdemo.models.NationalRegistryParty;

/**
 * Created by aegirt on 24.05.2016.
 */
public class NationalRegistryController extends BaseController {
    private static final String TAG = NationalRegistryController.class.getSimpleName();
    public static final int MESSAGE_SEARCH_REGISTRY = 1;
    public static final int MESSAGE_SEARCH_REGISTRY_COMPLETED = 2;

    private final List<NationalRegistryParty> mItems;

    public NationalRegistryController(FinTechApplication application, List<NationalRegistryParty> items) {
        super(application);
        this.mItems = items;
    }

    @Override
    public boolean handleMessage(int what, Object data) {
        Log.d(TAG, "handleMessage() called with: " + "what = [" + what + "], data = [" + data + "]");
        switch (what) {
            case MESSAGE_SEARCH_REGISTRY:
                searchRegistry((String) data);
                return true;
        }
        return false;
    }
    private void searchRegistry(String query) {
        new NationalRegistryQueryTask().execute(query);
    }

    private static String getKennitala(String query) {
        //dummy check
        if(query == null) return null;
        String kennitala = query.trim().replace("-", "");
        if(kennitala.length() == 10 && TextUtils.isDigitsOnly(kennitala)) {
            return kennitala;
        }
        return null;
    }

    private class NationalRegistryQueryTask extends AsyncTask<String, Void, AsyncTaskResult<List<NationalRegistryParty>>> {

        @Override
        protected AsyncTaskResult<List<NationalRegistryParty>> doInBackground(String... params) {
            AsyncTaskResult<List<NationalRegistryParty>> result = new AsyncTaskResult<>();

            try {
                FinTechApplication application = getApplication();
                String user = application.getCurrentUser();

                ArionServiceGateway gateway = new ArionServiceGateway(application.getOAuth(), user);
                List<NationalRegistryParty> listResult = new ArrayList<>();

                if(params[0] != null) {
                    String query = params[0].trim();
                    String kennitala = getKennitala(query);

                    if (kennitala != null) {
                        listResult.add(gateway.searchNationalRegistryByKennitala(kennitala));
                    } else {
                        listResult = gateway.searchNationalRegistryByName(query);
                    }
                }

                result.setResultValue(listResult);
            } catch (Exception e) {
                e.printStackTrace();
                result.setError(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<List<NationalRegistryParty>> result) {

            if (result.hasError()) {
                notifyMessageHandlers(MESSAGE_ERROR, result.getRelayMessage(), 0, result.getError());
                return;
            }
            List<NationalRegistryParty> results = result.getResultValue();

            synchronized (mItems) {
                mItems.clear();
                mItems.addAll(results);
                notifyMessageHandlers(MESSAGE_SEARCH_REGISTRY_COMPLETED, result.getRelayMessage(), 0, null);
            }
        }
    }
}
