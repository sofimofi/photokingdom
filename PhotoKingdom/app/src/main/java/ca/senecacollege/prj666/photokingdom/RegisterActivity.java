package ca.senecacollege.prj666.photokingdom;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirm;
    EditText editTextCity;
    Spinner spinnerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set title on ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.registration);

        // EditText and Spinner
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextConfirm = (EditText)findViewById(R.id.editTextConfirm);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);

        // Avatar
        findViewById(R.id.imageAvatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Need to choose an image for an avatar
                Toast.makeText(getApplicationContext(), "Image", Toast.LENGTH_SHORT).show();
            }
        });

        // Submit
        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRegister();
            }
        });
    }

    private void requestRegister() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirm = editTextConfirm.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        if (validateInputData(username, email, password, confirm, city, gender)) {
            Log.d(TAG,
                    "[requestRegister] Username: " + username +
                    ", Email: " + email +
                    ", Password: " + password +
                    ", Confirm: " + confirm +
                    ", City: " + city +
                    ", Gender: " + gender);
            // TODO: Need to request to register
        } else {
            Toast.makeText(this, "False", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputData(String username, String email, String password,
                                      String confirm, String city, String gender) {
        if (username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirm.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "All information is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.equals(confirm) == false) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
            return false;
        } else if (gender.equals(getResources().getString(R.string.gender))){
            Toast.makeText(this, "Choose a gender", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
