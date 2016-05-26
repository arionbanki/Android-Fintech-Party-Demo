package is.arionbanki.arionfintechdemo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import is.arionbanki.arionapi.ArionApiConstants;
import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.R;


//http://stackoverflow.com/questions/3149216/how-to-listen-for-a-webview-finishing-loading-a-url-in-android
public class AuthorizeActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = AuthorizeActivity.class.getSimpleName();
    FinTechApplication app;
    boolean isAuthorizing = false;
    private WebView mWebView;
    private int running = 0;
    private boolean hasError;
    private Snackbar mSnackbar;
    private View mRootView;

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);
            running = Math.max(running, 1);
            if (running == 1) {
                showOverlayProgress(getString(R.string.waiting_title));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: " + url);

            if (--running < 1 && !hasError) {
                hideInfo();
            }

            // the URL we're looking for looks like this:
            // https://localhost/?code=1234567890qwertyuiop
            Uri redirectUri = Uri.parse(app.getRedirectUri());
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (host != null && host.equals(redirectUri.getHost())) {
                String code = uri.getQueryParameter("code");

                if (!isAuthorizing && code != null) {
                    showOverlayProgress(null);
                    mWebView.setVisibility(View.INVISIBLE);
                    app.authorize(code, new Handler(AuthorizeActivity.this));
                    isAuthorizing = true;
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            Log.d(TAG, "shouldOverrideUrlLoading: " + urlNewString);
            running++;
            mWebView.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Uri uri = Uri.parse(failingUrl);
            String code = uri.getQueryParameter("code");

            //ignore failing url if the form is: https://localhost/?code=xxx
            if (TextUtils.equals("localhost", uri.getHost()) && !TextUtils.isEmpty(code)) {
                return;
            }

            Log.d(TAG, "onReceivedError() called with: " + "errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            hasError = true;
            showInfo(getString(R.string.transport_error));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (FinTechApplication) getApplication();
        setContentView(R.layout.activity_authorize);
        mRootView = findViewById(R.id.root_layout);
        if (mRootView == null) throw new IllegalStateException("root null");

        mSnackbar = Snackbar.make(mRootView, "", Snackbar.LENGTH_INDEFINITE);
        hideInfo();

        startAuthorization();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startAuthorization() {
        hasError = false;
        mWebView = (WebView) findViewById(R.id.web_view);
        if (mWebView != null) {
            mWebView.setWebViewClient(webViewClient);
            mWebView.setFocusable(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
        }

        String authURL = app.beginAuthorization();
        mWebView.loadUrl(authURL);
    }

    private void showOverlayProgress(String message) {
        Log.d(TAG, "showOverlayProgress() message = [" + message + "]");
        if (TextUtils.isEmpty(message)) {
            hideInfo();
        } else {
            showInfo(message);
        }
    }

    private void showInfo(String message) {
        Log.d(TAG, "showInfo() called with: " + "message = [" + message + "]");
        if (!TextUtils.isEmpty(message)) {
            mSnackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_INDEFINITE);
            mSnackbar.show();
        }
    }

    private void hideInfo() {
        Log.d(TAG, "hideInfo: HIDE");
        if(mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSnackbar = null;
    }

    @Override
    public boolean handleMessage(Message message) {
        Log.d(TAG, "handleMessage() called with: " + "message = [" + message + "]");
        isAuthorizing = false;
        switch (message.what) {
            case ArionApiConstants.MESSAGE_TOKEN_OK:
                Intent intent = new Intent(this, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                clearCookies();
                finish();
                break;
            case ArionApiConstants.MESSAGE_TOKEN_FAILED:
                //TODO handle error
                break;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.d(TAG, String.format("onReceiveValue() called with: value = [%s]", value));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        CookieManager.getInstance().flush();
                    }
                }
            });
        } else {
            CookieSyncManager manager = CookieSyncManager.createInstance(FinTechApplication.getContext());
            manager.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            manager.stopSync();
            manager.sync();
        }
    }
}
