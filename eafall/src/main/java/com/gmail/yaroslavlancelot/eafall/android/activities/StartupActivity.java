package com.gmail.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.multiplayer.GameServersListActivity;
import com.gmail.yaroslavlancelot.eafall.android.activities.settings.SettingsActivity;
import com.gmail.yaroslavlancelot.eafall.android.activities.singleplayer.PreGameCustomizationActivity;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.StartableIntent;

/**
 * first game activity with menu etc.
 */
public class StartupActivity extends BaseNonGameActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
        initCampaignButton(findViewById(R.id.campaign));
        initSingleGameButton(findViewById(R.id.single_game));
        initMultiplayerGameButton(findViewById(R.id.multiplayer_game));
        initSettingsButton(findViewById(R.id.settings));
        initExitButton(findViewById(R.id.exit));

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }

    private void initCampaignButton(View campaignButton) {
        if (campaignButton == null) return;
        if (campaignButton instanceof Button) {
            Button button = (Button) campaignButton;
            button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        }
        campaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StartableIntent campaignIntent = new CampaignIntent();
                campaignIntent.start(StartupActivity.this);
            }
        });
    }

    private void initSingleGameButton(View singleGameButton) {
        if (singleGameButton == null) return;
        if (singleGameButton instanceof Button) {
            Button button = (Button) singleGameButton;
            button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        }
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(StartupActivity.this, PreGameCustomizationActivity.class);
                startActivity(singleGameIntent);
            }
        });
    }

    private void initMultiplayerGameButton(View singleGameButton) {
        if (singleGameButton == null) return;
        if (singleGameButton instanceof Button) {
            Button button = (Button) singleGameButton;
            button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        }
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(StartupActivity.this, GameServersListActivity.class);
                startActivity(singleGameIntent);
            }
        });
    }

    private void initSettingsButton(View settingsButton) {
        if (settingsButton == null) return;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivityIntent = new Intent(StartupActivity.this, SettingsActivity.class);
                startActivity(settingsActivityIntent);
            }
        });
    }

    private void initExitButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StartupActivity.this.finish();
            }
        });
    }

    private LinearGradient getTextGradient(final int textHeight) {
        LinearGradient textGradient = new LinearGradient(
                0, 0, 0, textHeight,
                new int[]{getResources().getColor(R.color.startup_screen_button_text_top),
                        getResources().getColor(R.color.startup_screen_button_text_medium),
                        getResources().getColor(R.color.startup_screen_button_text_bottom)},
                new float[]{0, 0.5f, 1},
                Shader.TileMode.CLAMP);
        return textGradient;
    }
}
