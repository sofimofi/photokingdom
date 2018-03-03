package ca.senecacollege.prj666.photokingdom.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ca.senecacollege.prj666.photokingdom.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
