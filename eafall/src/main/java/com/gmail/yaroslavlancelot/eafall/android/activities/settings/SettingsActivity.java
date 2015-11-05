package com.gmail.yaroslavlancelot.eafall.android.activities.settings;

import android.os.Bundle;
import android.view.View;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.gmail.yaroslavlancelot.eafall.android.fragment.SettingsFragment;

/**
 * Settings Activity
 */
public class SettingsActivity extends BaseNonGameActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_layout);
        initSettingsFragment();
    }

    private void initSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        fragment.addBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SettingsActivity.this.finish();
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
