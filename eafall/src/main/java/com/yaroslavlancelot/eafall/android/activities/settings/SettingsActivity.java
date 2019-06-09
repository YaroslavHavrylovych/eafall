package com.yaroslavlancelot.eafall.android.activities.settings;

import android.os.Bundle;
import android.view.View;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.yaroslavlancelot.eafall.android.fragment.SettingsFragment;

/**
 * Settings Activity
 */
public class SettingsActivity extends BaseNonGameActivity {
    @Override
    protected void onCustomCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.holder_layout);
        initSettingsFragment();
    }

    @Override
    protected String getScreenName() {
        return "Settings Screen";
    }

    private void initSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        fragment.addBackButtonOnClickListener(v -> SettingsActivity.this.finish());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
