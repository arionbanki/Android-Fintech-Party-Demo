package is.arionbanki.arionfintechdemo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import is.arionbanki.arionapi.client.TransportException;
import is.arionbanki.arionapi.oauth2.CredentialsException;
import is.arionbanki.arionfintechdemo.AppData;
import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.fragments.AccountListFragment;
import is.arionbanki.arionfintechdemo.fragments.NationalRegistryFragment;
import is.arionbanki.arionfintechdemo.fragments.SettingsFragment;
import is.arionbanki.arionfintechdemo.interfaces.SearchListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawer;
    View mContainer;
    private Fragment currentFragment;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContainer = findViewById(R.id.fragment_container);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer == null) throw new IllegalStateException("drawer is null");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView == null) throw new IllegalStateException("mNavigationView is null");
        mNavigationView.setNavigationItemSelectedListener(this);
        displayFragment(FragmentTag.ACCOUNTS);
    }

    private void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;

        if (TextUtils.isEmpty(title)) {
            title = getResources().getString(R.string.app_name);
        }

        actionBar.setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean searchVisible = TextUtils.equals(FragmentTag.NATIONAL_REGISTRY, getCurrentFragment().getTag());
        menu.findItem(R.id.action_search).setVisible(searchVisible);
        return true;
    }

    private void displayFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = getFragmentInstance(tag);
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        ft.commitAllowingStateLoss();

        setCurrentFragment(fragment);

        Bundle arguments = fragment.getArguments();
        if (arguments != null && arguments.containsKey("title")) {
            setTitle(arguments.getString("title"));
        } else {
            setTitle(null);
        }

        setDrawerCheckedItem(fragment);

        invalidateOptionsMenu();
    }


    private void setDrawerCheckedItem(Fragment fragment) {
        if(fragment == null) return;

        if (FragmentTag.ACCOUNTS.equals(fragment.getTag())) {
            mNavigationView.setCheckedItem(R.id.nav_accounts);
        } else if (FragmentTag.NATIONAL_REGISTRY.equals(fragment.getTag())) {
            mNavigationView.setCheckedItem(R.id.nav_registry);
        } else if(FragmentTag.SETTINGS.equals(fragment.getTag())) {
            mNavigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setDrawerCheckedItem(getCurrentFragment());
    }

    private Fragment getFragmentInstance(String tag) {
        switch (tag) {
            case FragmentTag.ACCOUNTS:
                return AccountListFragment.newInstance();
            case FragmentTag.NATIONAL_REGISTRY:
                return NationalRegistryFragment.newInstance();
            case FragmentTag.SETTINGS :
                return SettingsFragment.newInstance();
            default:
                throw new IllegalStateException("Supply a fragment for the tag " + tag);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search_title));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accounts) {
            displayFragment(FragmentTag.ACCOUNTS);
        }
        if (id == R.id.nav_registry) {
            displayFragment(FragmentTag.NATIONAL_REGISTRY);
        } else if (id == R.id.nav_settings) {
            displayFragment(FragmentTag.SETTINGS);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean handleError(Exception ex) {
        Log.d(TAG, "handleError() called with: " + "ex = [" + ex + "]");
        if (ex instanceof CredentialsException) {
            handleCredentialsException((CredentialsException) ex);
            return true;
        } else if (ex instanceof TransportException) {
            handleTransportException();
            return true;
        }

        return false;
    }

    private void handleCredentialsException(CredentialsException ex) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (ex.isRefreshTokenExpired()) {
            builder.setTitle(getString(R.string.refresh_token_expired_caption));
            builder.setMessage(getString(R.string.re_authenticate_prompt));
            builder.setPositiveButton(getString(R.string.login_caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
        } else {
            builder.setTitle(getString(R.string.general_credential_error_title));
            builder.setMessage(getString(R.string.general_credential_error));
            builder.setNegativeButton(getString(R.string.login_caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            builder.setPositiveButton(getString(R.string.ok_caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        builder.create().show();
    }

    @SuppressWarnings("ConstantConditions")
    private void handleTransportException() {
        Snackbar.make(findViewById(R.id.fragment_container), R.string.error_could_not_connect, Snackbar.LENGTH_LONG).show();
    }

    public void logout() {
        FinTechApplication application = (FinTechApplication) getApplication();
        try {
            AppData.getInstance().delete();
            application.getOAuth().deleteUserCredential(application.getCurrentUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof SearchListener) {
            return ((SearchListener) fragment).onQueryTextSubmit(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof SearchListener) {
            return ((SearchListener) fragment).onQueryTextChange(newText);
        }
        return false;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    private static final class FragmentTag {
        public static final String ACCOUNTS = "AccountListFragment";
        public static final String NATIONAL_REGISTRY = "NationalRegistryFragment";
        public static final String SETTINGS = "SettingsFragment";
    }
}
