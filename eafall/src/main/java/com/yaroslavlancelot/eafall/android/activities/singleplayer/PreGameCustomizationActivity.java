package com.yaroslavlancelot.eafall.android.activities.singleplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yaroslavlancelot.eafall.android.activities.PreGameCustomizationBaseActivity;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

public class PreGameCustomizationActivity extends PreGameCustomizationBaseActivity {
    @Override
    protected void onCustomCreate(@Nullable final Bundle savedInstanceState) {
        super.onCustomCreate(savedInstanceState);
        initStartButton();
    }

    private void initStartButton() {
        mStartTheGame.setOnClickListener(getStartButtonOnClick(
                SinglePlayerGameActivity.class, IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE
        ));
    }
}
