package com.gmail.yaroslavlancelot.eafall.android.activities.settings;

import android.os.Bundle;
import android.view.View;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.gmail.yaroslavlancelot.eafall.general.settings.SettingsFragment;

/**
 * Settings Activity
 */
public class SettingsActivity extends BaseNonGameActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        initSettingsFragment();
        initBackButton(findViewById(R.id.back));
    }

    private void initSettingsFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    private void initBackButton(View backButton) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
}
