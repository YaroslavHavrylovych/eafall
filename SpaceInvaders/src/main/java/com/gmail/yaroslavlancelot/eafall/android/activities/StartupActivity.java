package com.gmail.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.multiplayer.GameServersListActivity;
import com.gmail.yaroslavlancelot.eafall.android.activities.singleplayer.PreGameCustomizationActivity;
import com.gmail.yaroslavlancelot.eafall.game.campaign.CampaignFactory;
import com.gmail.yaroslavlancelot.eafall.game.campaign.ICampaign;

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
        initExitButton(findViewById(R.id.exit));
    }

    @Override
    public void onBackPressed() {
        StartupActivity.this.finish();
    }

    private void initCampaignButton(View campaignButton) {
        if (campaignButton == null) return;
        campaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ICampaign campaign =
                        CampaignFactory.getInstance().getCampaign(
                                CampaignFactory.TypeCampaign.GUIDE_CAMPAIGN);
                campaign.startCampaignActivity(StartupActivity.this);
            }
        });
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
