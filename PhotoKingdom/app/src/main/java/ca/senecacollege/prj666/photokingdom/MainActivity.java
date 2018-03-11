package ca.senecacollege.prj666.photokingdom;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import ca.senecacollege.prj666.photokingdom.fragments.PingsFragment;
import ca.senecacollege.prj666.photokingdom.fragments.SettingsFragment;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;

public class MainActivity extends AppCompatActivity {

    // Fragments
    private LiveFeedFragment mLiveFeedFragment;
    private MapContainerFragment mMapContainerFragment;
    private UserFragment mUserFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Call FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // TODO: Select an item on navigation when selected fragment created to go to back
            // switch to selected fragment
            switch (item.getItemId()) {
                case R.id.navigation_livefeed:
                    if (mLiveFeedFragment == null) {
                        mLiveFeedFragment = new LiveFeedFragment();
                    }

                    transaction.replace(R.id.frameLayout, mLiveFeedFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_map:
                    if (mMapContainerFragment == null) {
                        mMapContainerFragment = new MapContainerFragment();
                    }

                    transaction.replace(R.id.frameLayout, mMapContainerFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_user:
                    // Check resident logged-in
                    ResidentSessionManager sessionManager =
                            new ResidentSessionManager(getApplicationContext());
                    if (sessionManager.isLoggedIn()) {
                        if (mUserFragment == null) {
                            mUserFragment = new UserFragment();
                        }

                        transaction.replace(R.id.frameLayout, mUserFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_visitor_doesnot_have_profile, Toast.LENGTH_LONG).show();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Switch to live feed by default
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, new LiveFeedFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                // Move to SettingsFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.logout:
                // Logout resident
                ResidentSessionManager manager = new ResidentSessionManager(this);
                manager.logoutResident();

                // Move to LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();

                return true;
            // TODO: Remove test menu
            case R.id.test_ping_list:
                // Move to PingsFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new PingsFragment())
                        .addToBackStack(null)
                        .commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MapContainerFragment.PERMISSIONS_REQUEST_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Initialize Google Map if requested permissions granted
                if (mMapContainerFragment != null) {
                    mMapContainerFragment.initMapWithCurrentLocation();
                }
            }
        }
    }
}
