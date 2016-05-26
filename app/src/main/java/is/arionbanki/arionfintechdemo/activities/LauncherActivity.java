package is.arionbanki.arionfintechdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import is.arionbanki.arionfintechdemo.FinTechApplication;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = LauncherActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FinTechApplication application = (FinTechApplication) getApplication();

        if (application.hasOAuthCredential()) {
            startMainActivity();
        } else {
            startAuthorization();
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void startAuthorization() {
        Intent intent = new Intent(this, AuthorizeActivity.class);
        startActivity(intent);
        finish();
    }
}
