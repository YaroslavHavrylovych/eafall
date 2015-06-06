package com.gmail.yaroslavlancelot.eafall.android.activities.singleplayer;

import android.os.Bundle;

import com.gmail.yaroslavlancelot.eafall.android.activities.PreGameCustomizationBaseActivity;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;

public class PreGameCustomizationActivity extends PreGameCustomizationBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStartButton();
    }

    private void initStartButton() {
        mStartTheGame.setOnClickListener(getStartButtonOnClick(
                SinglePlayerGameActivity.class, ITeam.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                ITeam.ControlType.BOT_CONTROL_ON_SERVER_SIDE
        ));
    }
}
