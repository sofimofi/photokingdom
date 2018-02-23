package ca.senecacollege.prj666.photokingdom;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import ca.senecacollege.prj666.photokingdom.fragments.CityDialogFragment;
import ca.senecacollege.prj666.photokingdom.models.AvatarImage;
import ca.senecacollege.prj666.photokingdom.models.City;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to create user
 */
public class RegisterActivity extends AppCompatActivity
        implements CityDialogFragment.OnCitySelectedListener {
    private static final String TAG = "RegisterActivity";

    // Request code
    private static final int ACTION_PICK_REQUEST = 1;

    // Avatar image uri
    private Uri mAvatarUri;

    // Resident to register
    private Resident mResident;
    private City mCity;

    // Service for Retrofit
    PhotoKingdomService service;

    // Widgets
    private EditText mEditTextUsername;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirm;
    private EditText mEditTextCity;
    private Spinner mSpinnerGender;
    private ImageView mImageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set title on ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.registration);

        // EditText and Spinner
        mEditTextUsername = (EditText)findViewById(R.id.editTextUsername);
        mEditTextEmail = (EditText)findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText)findViewById(R.id.editTextPassword);
        mEditTextConfirm = (EditText)findViewById(R.id.editTextConfirm);
        mEditTextCity = (EditText)findViewById(R.id.editTextCity);
        mSpinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        mImageViewAvatar = (ImageView)findViewById(R.id.imageAvatar);

        // Create a retrofit service for PhotoKingdomAPI
        service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);

        // Avatar
        mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show images from devices
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ACTION_PICK_REQUEST);
            }
        });

        // Submit
        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (service != null) {
                    requestRegister();
                }
            }
        });

        // City
        mEditTextCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a dialog to select a city
                CityDialogFragment dialog = new CityDialogFragment();
                dialog.show(getSupportFragmentManager(), "CityDialogFragment");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                // Get a image uri and set the image to an ImageView
                mAvatarUri = data.getData();
                mImageViewAvatar.setImageURI(mAvatarUri);
            }
        }
    }

    /**
     * Request to register a user
     */
    private void requestRegister() {
        // Get input data
        String username = mEditTextUsername.getText().toString().trim();
        String email = mEditTextEmail.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        String confirm = mEditTextConfirm.getText().toString().trim();
        int genderPos = mSpinnerGender.getSelectedItemPosition();
        String gender = getGenderFromPosition(genderPos);

        // Validate user input
        if (validateInputData(username, email, password, confirm, genderPos)) {
            mResident = new Resident(username, email, password, gender, mCity.getId());

            // Create a user
            if (mAvatarUri != null) {
                createResidentWithAvatar();
            } else {
                createResident(null);
            }
        }
    }

    /**
     * Get a gender string from a position
     * @param position
     * @return String
     */
    private String getGenderFromPosition(int position) {
        switch (position) {
            case 1:
                return "M";
            case 2:
                return "F";
        }

        return null;
    }

    /**
     * Validate entered all user information
     * @param username
     * @param email
     * @param password
     * @param confirm
     * @param genderPos
     * @return boolean
     */
    private boolean validateInputData(String username, String email, String password,
                                      String confirm, int genderPos) {
        // TODO: Validate email type and the email doesn't exist
        if (username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirm.isEmpty() || mCity == null) {
            Toast.makeText(this, R.string.required_all_information, Toast.LENGTH_LONG).show();
            return false;
        } else if (password.equals(confirm) == false) {
            Toast.makeText(this, R.string.password_not_match, Toast.LENGTH_LONG).show();
            return false;
        } else if (genderPos == 0){
            Toast.makeText(this, R.string.choose_gender, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Get an image path on the device from URI
     * @param imageUri
     * @return String
     */
    private String getRealPathFromURI(Uri imageUri) {
        String result;
        Cursor cursor = getContentResolver().query(imageUri,
                null, null, null, null);

        if (cursor == null) {
            result = imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(columnIndex);
            cursor.close();
        }

        return result;
    }

    /**
     * Create a user with an avatar image
     */
    private void createResidentWithAvatar() {
        if (mAvatarUri != null) {
            // Create a multipart request body
            File file = new File(getRealPathFromURI(mAvatarUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(mAvatarUri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            // Execute a request to upload an avatar image
            Call<AvatarImage> uploadImageCall = service.uploadAvatarImage(body);
            uploadImageCall.enqueue(new Callback<AvatarImage>() {
                @Override
                public void onResponse(Call<AvatarImage> call, Response<AvatarImage> response) {
                    if (response.isSuccessful()) {
                        String avatarPath = response.body().getPath();

                        // Create a resident with a uploaded avatar image path
                        createResident(avatarPath);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.avatar_upload_fail, Toast.LENGTH_LONG).show();

                        // Create a resident without an avatar image
                        createResident(null);
                    }
                }

                @Override
                public void onFailure(Call<AvatarImage> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.avatar_upload_fail, Toast.LENGTH_LONG).show();

                    // Create a resident without an avatar image
                    createResident(null);
                }
            });
        }
        else {
            // Create a resident without an avatar image
            createResident( null);
        }
    }

    /**
     * Call PhotoKingdomAPI to create a resident with an avatar image
     * @param avatarPath
     */
    private void createResident(String avatarPath) {
        if (mResident == null) {
            Toast.makeText(getApplicationContext(), R.string.user_register_failed, Toast.LENGTH_LONG).show();
            return;
        }

        // Set an avatar to a resident
        if (avatarPath != null) {
            mResident.setAvatarImagePath(avatarPath);
        }

        // Execute a request to create a resident
        Call<Resident> call = service.createResident(mResident);
        call.enqueue(new Callback<Resident>() {
            @Override
            public void onResponse(Call<Resident> call, Response<Resident> response) {

                if (response.isSuccessful()) {
                    String email = response.body().getEmail();
                    Toast.makeText(getApplicationContext(), "[" + email + "] registered", Toast.LENGTH_LONG).show();

                    // Move to LoginActivity
                    // TODO: Change to login automatically after login function implemented
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), R.string.user_register_failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                Log.d(TAG, "[createResident:onFailure] " + t.getMessage());
            }
        });
    }

    @Override
    public void onCitySelected(City city) {
        // Set a city
        mCity = city;
        mEditTextCity.setText(city.getName());
    }
}