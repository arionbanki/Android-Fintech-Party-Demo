package is.arionbanki.arionfintechdemo.controllers;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import is.arionbanki.arionapi.utils.AsyncTaskResult;
import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.gateways.ArionServiceGateway;
import is.arionbanki.arionfintechdemo.models.Account;

/**
 * Created by aegirt on 24.05.2016.
 */
public class AccountController extends BaseController {
    private static final String TAG = AccountController.class.getSimpleName();
    public static final int MESSAGE_GET_ACCOUNT_SINGLE = 1;
    public static final int MESSAGE_GET_ACCOUNT_SINGLE_COMPLETED = 2;
    public static final int MESSAGE_GET_ACCOUNTS = 3;
    public static final int MESSAGE_GET_ACCOUNTS_COMPLETED = 4;

    private final List<Account> mItems;

    public AccountController(FinTechApplication application, List<Account> items) {
        super(application);
        this.mItems = items;
    }

    @Override
    public boolean handleMessage(int what, Object data) {
        Log.d(TAG, "handleMessage() called with: " + "what = [" + what + "], data = [" + data + "]");
        switch (what) {
            case MESSAGE_GET_ACCOUNTS:
                getAccounts();
                return true;
            case MESSAGE_GET_ACCOUNT_SINGLE :
                getAccountById((String)data);
                return true;
        }
        return false;
    }

    private void getAccountById(String accountId) {
        throw new RuntimeException("Not Implemented");
    }

    private void getAccounts() {
        new AccountsListTask().execute();
    }

    private class AccountsListTask extends AsyncTask<String, Void, AsyncTaskResult<List<Account>>> {

        @Override
        protected AsyncTaskResult<List<Account>> doInBackground(String... params) {
            AsyncTaskResult<List<Account>> result = new AsyncTaskResult<>();

            try {
                FinTechApplication application = getApplication();
                String user = application.getCurrentUser();

                ArionServiceGateway gateway = new ArionServiceGateway(application.getOAuth(), user);
                List<Account> listResult = gateway.getAllAccounts();

                result.setResultValue(listResult);
            } catch (Exception e) {
                e.printStackTrace();
                result.setError(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<List<Account>> result) {

            if (result.hasError()) {
                notifyMessageHandlers(MESSAGE_ERROR, result.getRelayMessage(), 0, result.getError());
                return;
            }
            List<Account> results = result.getResultValue();

            synchronized (mItems) {
                mItems.clear();
                mItems.addAll(results);
                notifyMessageHandlers(MESSAGE_GET_ACCOUNTS_COMPLETED, result.getRelayMessage(), 0, null);
            }
        }
    }
}
