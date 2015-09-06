package com.gmail.yaroslavlancelot.eafall.android.activities.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.gmail.yaroslavlancelot.eafall.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
