package com.yaroslavlancelot.eafall.android.activities.singleplayer;

import android.os.Bundle;

import com.yaroslavlancelot.eafall.android.activities.PreGameCustomizationBaseActivity;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

public class PreGameCustomizationActivity extends PreGameCustomizationBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStartButton();
    }

    private void initStartButton() {
        mStartTheGame.setOnClickListener(getStartButtonOnClick(
                SinglePlayerGameActivity.class, IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE
        ));
    }
}
