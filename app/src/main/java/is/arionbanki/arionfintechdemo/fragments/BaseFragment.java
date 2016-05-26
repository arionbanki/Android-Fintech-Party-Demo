package is.arionbanki.arionfintechdemo.fragments;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.activities.MainActivity;

/**
 * Created by aegirt on 24.05.2016.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected void handleException(Message message) {
        Log.d(TAG, "handleException() called with: " + "message = [" + message + "]");
        if (message.obj != null && message.obj instanceof Exception) {
            MainActivity activity = (MainActivity) getActivity();
            activity.handleError((Exception) message.obj);
        } else {
            Log.d(TAG, "handleException() UNHANDLED - message = [" + message + "]");
        }
    }

    public FinTechApplication getFinTechApplication() {
        return (FinTechApplication) getActivity().getApplication();
    }
}
