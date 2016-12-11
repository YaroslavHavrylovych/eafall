package com.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.android.activities.settings.SettingsActivity;
import com.yaroslavlancelot.eafall.android.activities.singleplayer.PreGameCustomizationActivity;
import com.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * first game activity with menu etc.
 */
public class StartupActivity extends BaseNonGameActivity {
    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }

    @Override
    protected void onCustomCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.startup_layout);
        initCampaignButton(findViewById(R.id.campaign));
        initSingleGameButton(findViewById(R.id.single_game));
        initMultiplayerGameButton(findViewById(R.id.multiplayer_game));
        initSettingsButton(findViewById(R.id.settings));
        initExitButton(findViewById(R.id.exit));

        PreferenceManager.setDefaultValues(EaFallApplication.getContext(), R.xml.preferences, false);
    }

    private void initCampaignButton(View campaignButton) {
        Button button = (Button) campaignButton;
        button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        button.setText(R.string.campaign);
        campaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StartableIntent startableIntent;
                String intentClassName = "com.yaroslavlancelot.eafall.game.campaign" +
                        ".intents.CampaignIntent";
                Class cls;
                try {
                    cls = Class.forName(intentClassName);
                    startableIntent = (StartableIntent) cls.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                startableIntent.start(StartupActivity.this);
            }
        });
    }

    private void initSingleGameButton(View singleGameButton) {
        Button button = (Button) singleGameButton;
        button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = PreGameCustomizationActivity.getSinglePlayerIntent(
                        SinglePlayerGameActivity.class,
                        Mutants.ALLIANCE_NAME, Mutants.ALLIANCE_NAME,
                        IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                        IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE);
                startActivity(intent);
            }
        });
    }

    private void initMultiplayerGameButton(View singleGameButton) {
        Button button = (Button) singleGameButton;
        button.setText(R.string.sandbox);
        button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StartableIntent startableIntent;
                String intentClassName = "com.yaroslavlancelot.eafall.game.sandbox" +
                        ".intents.SandboxIntent";
                Class cls;
                try {
                    cls = Class.forName(intentClassName);
                    startableIntent = (StartableIntent) cls.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                startableIntent.start(StartupActivity.this);
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

    public static LinearGradient getTextGradient(final int textHeight) {
        LinearGradient textGradient = new LinearGradient(
                0, 0, 0, textHeight,
                new int[]{EaFallApplication.getContext().getResources().getColor(R.color.startup_screen_button_text_top),
                        EaFallApplication.getContext().getResources().getColor(R.color.startup_screen_button_text_medium),
                        EaFallApplication.getContext().getResources().getColor(R.color.startup_screen_button_text_bottom)},
                new float[]{0, 0.5f, 1},
                Shader.TileMode.CLAMP);
        return textGradient;
    }
}
