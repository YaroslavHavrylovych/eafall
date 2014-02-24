package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.util.color.Color;

public class ClientGameActivity extends MainOperationsBaseGameActivity {
    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        //no physic body at client
    }

    @Override
    protected void onLoadGameResources() {
        // user
        Color teamColor = Color.RED;
        IRace userRace = new Imperials(teamColor, this, this);
        userRace.loadResources(getTextureManager(), this);
        mRedTeam = createTeam(teamColor, userRace, GameStringsConstantsAndUtils.RED_TEAM_NAME);
        // bot
        teamColor = Color.BLUE;
        IRace botRace = new Imperials(teamColor, this, this);
        botRace.loadResources(getTextureManager(), this);
        mBlueTeam = createTeamWithMoneyUpdate(teamColor, botRace, GameStringsConstantsAndUtils.BLUE_TEAM_NAME);

        super.onLoadGameResources();
    }

    @Override
    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                onLoadGameResources();

                onInitScene();
                onInitSceneObjects(true, false);

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);
            }
        }));
    }
}
