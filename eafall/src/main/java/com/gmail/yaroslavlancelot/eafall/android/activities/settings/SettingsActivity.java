package com.gmail.yaroslavlancelot.eafall.android.activities.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;

/**
 * Settings Activity
 */
public class SettingsActivity extends BaseNonGameActivity {

    public static final String KEY_PREF_DEVELOPERS_MODE = "pref_key_dev_mode";
    public static final String KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR = "pref_key_unit_health_bar_behavior";
    public static final String KEY_PREF_SOUNDS = "pref_key_game_sounds";
    public static final String KEY_PREF_MUSIC = "pref_key_music";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        initSettingsFragment();
        initBackButton(findViewById(R.id.back));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initSettingsFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    private void initBackButton(View backButton) {
        if (backButton == null) return;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
}
