package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.gmail.yaroslavlancelot.spaceinvaders.R;

public class StartupActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
        initSingleGameButton(findViewById(R.id.single_game));
        initExitButton(findViewById(R.id.exit));
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

    private void initSingleGameButton(View singleGameButton) {
        if (singleGameButton == null) return;
        singleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(StartupActivity.this, GameActivity.class);
                startActivity(singleGameIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }
}
