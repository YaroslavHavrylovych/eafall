package com.yaroslavlancelot.eafall.android.activities.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.yaroslavlancelot.eafall.android.fragment.GameSettingsFragment;
import com.yaroslavlancelot.eafall.android.fragment.UserInfoSettingsFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Settings Activity
 */
public class SettingsActivity extends BaseNonGameActivity {
    /** settings on screen back button */
    private ImageButton mBackButton;
    private ImageButton mInfoButton;
    private TextView mTitle;
    private @NotNull Fragment gameSettingsFragment = new GameSettingsFragment();
    private @NotNull Fragment userInfoSettingsFragment = new UserInfoSettingsFragment();

    @Override
    protected void onCustomCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.settings_holder_layout);
        //header
        mTitle = findViewById(R.id.title_text);
        //back button
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this::onBackButtonClick);
        //info
        mInfoButton = findViewById(R.id.information_button);
        mInfoButton.setVisibility(View.VISIBLE);
        mInfoButton.setOnClickListener(this::onInfoButtonClick);
        //fragments
        initSettingsFragment();
    }

    @Override
    protected String getScreenName() {
        return "Settings Screen";
    }

    private void onBackButtonClick(View view) {
        finish();
    }

    private void onInfoButtonClick(View view) {
        mTitle.setText(R.string.policy_text);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.scale_move_from_left, R.anim.scale_move_left)
                .replace(R.id.content, userInfoSettingsFragment)
                .commit();
        mInfoButton.setVisibility(View.GONE);
    }

    private void initSettingsFragment() {
        mTitle.setText(R.string.settings);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, gameSettingsFragment)
                .commit();
    }
}
