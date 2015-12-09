package com.gmail.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.yaroslavlancelot.eafall.BuildConfig;
import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.StartableIntent;
import com.gmail.yaroslavlancelot.eafall.android.activities.settings.SettingsActivity;
import com.gmail.yaroslavlancelot.eafall.android.activities.singleplayer.PreGameCustomizationActivity;
import com.gmail.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * first game activity with menu etc.
 */
public class StartupActivity extends BaseNonGameActivity {
    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
        initCampaignButton(findViewById(R.id.campaign));
        initSingleGameButton(findViewById(R.id.single_game));
        initMultiplayerGameButton(findViewById(R.id.multiplayer_game));
        initSettingsButton(findViewById(R.id.settings));
        initExitButton(findViewById(R.id.exit));

        PreferenceManager.setDefaultValues(EaFallApplication.getContext(), R.xml.preferences, false);
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
                String intentClassName;
                if (BuildConfig.DEMO_VERSION) {
                    Toast.makeText(StartupActivity.this, R.string.not_int_demo, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    intentClassName = "com.gmail.yaroslavlancelot.eafall.game.campaign" +
                            ".intents.CampaignIntent";
                    Class cls;
                    StartableIntent startableIntent;
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
                if (BuildConfig.DEMO_VERSION) {
                    Intent intent = PreGameCustomizationActivity.getSinglePlayerIntent(
                            SinglePlayerGameActivity.class,
                            Mutants.ALLIANCE_NAME, Mutants.ALLIANCE_NAME,
                            IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                            IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE);
                    startActivity(intent);
                } else {
                    Intent singleGameIntent = new Intent(StartupActivity.this,
                            PreGameCustomizationActivity.class);
                    startActivity(singleGameIntent);
                }
            }
        });
    }

    private void initMultiplayerGameButton(View singleGameButton) {
        if (singleGameButton == null) return;
        Button button = (Button) singleGameButton;
        if (BuildConfig.DEMO_VERSION) {
            button.setText(R.string.sandbox);
        } else {
            button.setText(R.string.multiplayer_game);
        }
        button.getPaint().setShader(getTextGradient(button.getLineHeight()));
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StartableIntent startableIntent;
                if (BuildConfig.DEMO_VERSION) {
                    String intentClassName = "com.gmail.yaroslavlancelot.eafall.game.sandbox" +
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
                } else {
                    Class cls;
                    try {
                        cls = Class.forName("com.gmail.yaroslavlancelot.eafall.android" +
                                ".activities.multiplayer.GameServersListActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    startableIntent = new StartableIntent(StartupActivity.this, cls);
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
