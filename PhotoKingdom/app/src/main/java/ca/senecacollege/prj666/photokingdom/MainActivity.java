package ca.senecacollege.prj666.photokingdom;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import ca.senecacollege.prj666.photokingdom.fragments.AttractionDetailsFragment;
import ca.senecacollege.prj666.photokingdom.fragments.PhotowarQueueFragment;
import ca.senecacollege.prj666.photokingdom.fragments.PingsFragment;
import ca.senecacollege.prj666.photokingdom.fragments.SettingsFragment;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;

public class MainActivity extends AppCompatActivity implements PhotowarFragment.OnPhotowarFragmentInteractionListener {

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
            Fragment fragment = null;

            // set corresponding title and bottom nav highlight using back button
            fragmentManager.addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        public void onBackStackChanged() {
                            // Update your UI here.
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                            ActionBar actionBar = getSupportActionBar();
                            BottomNavigationView nav = findViewById(R.id.navigation);

                            if(f instanceof LiveFeedFragment){
                                actionBar.setTitle(R.string.livefeed);
                                nav.getMenu().findItem(R.id.navigation_livefeed).setChecked(true);
                            }else if(f instanceof MapContainerFragment) {
                                actionBar.setTitle(R.string.map);
                                nav.getMenu().findItem(R.id.navigation_map).setChecked(true);
                            }else if(f instanceof UserFragment){
                                actionBar.setTitle(R.string.userprofile);
                                nav.getMenu().findItem(R.id.navigation_user).setChecked(true);
                            }
                        }
                    });

            // TODO: Select an item on navigation when selected fragment created to go to back
            // switch to selected fragment
            switch (item.getItemId()) {
                case R.id.navigation_livefeed:
                    //fragment = new LiveFeedFragment();
                    if (mLiveFeedFragment == null) {
                        mLiveFeedFragment = new LiveFeedFragment();
                    }
                    fragment = mLiveFeedFragment;
                    break;
                case R.id.navigation_map:
                    //fragment = new MapContainerFragment();
                    if (mMapContainerFragment == null) {
                        mMapContainerFragment = new MapContainerFragment();
                    }
                    fragment = mMapContainerFragment;
                    break;
                case R.id.navigation_user:
                    // Check resident logged-in
                    ResidentSessionManager sessionManager =
                            new ResidentSessionManager(getApplicationContext());
                    if (sessionManager.isLoggedIn()) {
                        //fragment = new UserFragment();
                        if (mUserFragment == null) {
                            mUserFragment = new UserFragment();
                        }
                        fragment = mUserFragment;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_visitor_doesnot_have_profile, Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            if(fragment != null){
                transaction.replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        // prevent press back to exit program
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            //Toast.makeText(getApplicationContext(),"already at first frame", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Switch to live feed by default if no fragment is presented
        if(getSupportFragmentManager().findFragmentById(R.id.frameLayout) == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LiveFeedFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        invalidateOptionsMenu();
        ResidentSessionManager manager = new ResidentSessionManager(this);
        if (manager.isLoggedIn()){
            menu.findItem(R.id.logout).setTitle(R.string.logout);
        }else{
            menu.findItem(R.id.logout).setTitle(R.string.login);
        }
        return super.onPrepareOptionsMenu(menu);
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
            // TODO: Remove test menus
            case R.id.test_photowar_queue:
                // Move to PhotowarQueueFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new PhotowarQueueFragment())
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.test_photowar_detail:
                PhotowarFragment photowarFragment = PhotowarFragment.newInstance(34);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, photowarFragment )
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

    @Override
    public void onFragmentInteraction() {

    }
}
