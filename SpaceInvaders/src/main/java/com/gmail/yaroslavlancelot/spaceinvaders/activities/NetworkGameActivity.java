package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;

public class NetworkGameActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_game_layout);
        initBackButton(findViewById(R.id.games_list_back_button));
        initGamesList((ListView) findViewById(R.id.games_list_list_view));
    }

    private void initGamesList(ListView listView) {
        if (listView == null) return;
    }

    private void initBackButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NetworkGameActivity.this.finish();
            }
        });
    }
}
