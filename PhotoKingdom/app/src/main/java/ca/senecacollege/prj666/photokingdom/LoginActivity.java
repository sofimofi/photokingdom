package ca.senecacollege.prj666.photokingdom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ca.senecacollege.prj666.photokingdom.models.LoginInfo;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Login view
 * This is the first view
 * Open Main view and close this view if user logged-in
 *
 * @author Wonho
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    // Widgets
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private ProgressBar mProgressBar;

    // Login
    private Resident mResident;
    private ResidentSessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isGoogleServiceAvailable()) {
            setContentView(R.layout.activity_login);

            // Email and password
            mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
            mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
            mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

            // Check login
            mSessionManager = new ResidentSessionManager(this);
            if (mSessionManager.isLoggedIn()) {
                // Auto-login preference in Settings
                SharedPreferences settingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                boolean isAutoLogin = settingsPrefs.getBoolean("pref_auto_login", true);
                if (isAutoLogin) {
                    startMainActivity();
                } else {
                    mEditTextEmail.setText(mSessionManager.getResident().getEmail());
                    mEditTextPassword.requestFocus();
                }
            }

            // Login
            findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestLogin();
                }
            });

            // Visitor
            findViewById(R.id.buttonVisitor).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Visitor button clicked");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

            // Register
            findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void requestLogin() {
        String email = mEditTextEmail.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.msg_enter_email_password, Toast.LENGTH_LONG).show();
        } else {
            checkLogin(email, password);
        }
    }
    private void checkLogin(String email, String password) {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        LoginInfo info = new LoginInfo(email, password);

        showProgressBar();

        Call<Resident> call = service.loginResident(info);
        call.enqueue(new Callback<Resident>() {
            @Override
            public void onResponse(Call<Resident> call, Response<Resident> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    // Login
                    mResident = response.body();
                    loginResident();
                } else {
                    try {
                        Log.d(TAG, "[checkLogin] " + response.errorBody().string());
                        logoutResident();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                Log.d(TAG, "[checkLogin:onFailure]" + t.getMessage());
                hideProgressBar();
                logoutResident();
            }
        });
    }

    private void loginResident() {
        if (mSessionManager != null) {
            if (mResident != null) {
                mSessionManager.loginResident(mResident);
                startMainActivity();
            } else {
                logoutResident();
            }
        }
    }

    private void logoutResident() {
        if (mSessionManager != null) {
            mSessionManager.logoutResident();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public boolean isGoogleServiceAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }else{
            Toast.makeText(this, "Cannot connect google play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
