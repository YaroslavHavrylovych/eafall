package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.multiplayer.GameServersListActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.singleplayer.PreGameCustomizationActivity;

/**
 * first game activity with menu etc.
 */
public class StartupActivity extends BaseNonGameActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
        initSingleGameButton(findViewById(R.id.single_game));
        initMultiplayerGameButton(findViewById(R.id.multiplayer_game));
        initExitButton(findViewById(R.id.exit));
    }

    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }

    private void initSingleGameButton(View singleGameButton) {
        if (singleGameButton == null) return;
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
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(StartupActivity.this, GameServersListActivity.class);
                startActivity(singleGameIntent);
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
}
