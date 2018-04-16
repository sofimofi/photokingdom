package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ca.senecacollege.prj666.photokingdom.models.Resident;

/**
 * Save and load resident information
 * Login and logout a resident
 *
 * @author Wonho
 */
public class ResidentSessionManager {
    private static final String TAG = "LoginSessionManager";

    // SharedPreferences
    private static final String PREF_RESIDENT_SESSION = "ResidentSessionPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AVATAR_IMAGE_PATH = "avatarImagePath";
    private static final String KEY_CITY_ID = "cityId";
    private static final String KEY_CITY_NAME = "cityName";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TOTAL_POINTS = "totalPoints";

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    public ResidentSessionManager(Context context) {
        this.mContext = context;
        mSharedPref = mContext.getSharedPreferences(PREF_RESIDENT_SESSION, Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        mEditor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        mEditor.commit();
    }

    public boolean isLoggedIn(){
        return mSharedPref.getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public void setResident(Resident resident) {
        mEditor.putInt(KEY_ID, resident.getId());
        mEditor.putString(KEY_USERNAME, resident.getUserName());
        mEditor.putString(KEY_GENDER, resident.getGender());
        mEditor.putString(KEY_EMAIL, resident.getEmail());
        mEditor.putString(KEY_AVATAR_IMAGE_PATH, resident.getAvatarImagePath());
        mEditor.putInt(KEY_CITY_ID, resident.getCityId());
        mEditor.putString(KEY_CITY_NAME, resident.getCity());
        mEditor.putString(KEY_TITLE, resident.getTitle());
        mEditor.putLong(KEY_TOTAL_POINTS, resident.getTotalPoints());

        mEditor.commit();
    }

    public Resident getResident() {
        Resident resident = new Resident();
        resident.setId(mSharedPref.getInt(KEY_ID, 0));
        resident.setUserName(mSharedPref.getString(KEY_USERNAME, null));
        resident.setGender(mSharedPref.getString(KEY_GENDER, null));
        resident.setEmail(mSharedPref.getString(KEY_EMAIL, null));
        resident.setAvatarImagePath(mSharedPref.getString(KEY_AVATAR_IMAGE_PATH, null));
        resident.setCityId(mSharedPref.getInt(KEY_CITY_ID, 0));
        resident.setCity(mSharedPref.getString(KEY_CITY_NAME, null));
        resident.setTitle(mSharedPref.getString(KEY_TITLE, null));
        resident.setTotalPoints(mSharedPref.getLong(KEY_TOTAL_POINTS, 0));

        return resident;
    }


    public void loginResident(Resident resident) {
        setResident(resident);
        setLogin(true);
    }

    public void logoutResident() {
        mEditor.clear();
        mEditor.commit();
        setLogin(false);
    }
}
