package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;
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
        IRace userRace = new Imperials(Color.RED, this, this);
        userRace.loadResources(getTextureManager(), this);
        mRedTeam = new Team(GameStringsConstantsAndUtils.RED_TEAM_NAME, userRace) {
            @Override
            public void changeMoney(final int delta) {
                super.changeMoney(delta);
                updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, this)); // this - red team
            }
        };
        mRedTeam.setTeamColor(Color.RED);

        // bot
        IRace botRace = new Imperials(Color.BLUE, this, this);
        botRace.loadResources(getTextureManager(), this);
        mBlueTeam = new Team(GameStringsConstantsAndUtils.BLUE_TEAM_NAME, botRace);
        mBlueTeam.setTeamColor(Color.BLUE);
        initUser(mBlueTeam);

        super.onLoadGameResources();
    }

    @Override
    protected void initTeams() {
        // user
        mRedTeam = new Team(GameStringsConstantsAndUtils.RED_TEAM_NAME, redTeamUserRace) {
            @Override
            public void changeMoney(final int delta) {
                super.changeMoney(delta);
                updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, this)); // this - red team
            }
        };
        mRedTeam.setTeamColor(Color.RED);
        initUser(mRedTeam);
        // bot
        mBlueTeam = new Team(GameStringsConstantsAndUtils.BLUE_TEAM_NAME, blueTeamUserRace);
        mRedTeam.setTeamColor(Color.BLUE);
    }

    @Override
    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                onLoadGameResources();

                onInitGameScene();
                onInitPlanetsSetEnemies(true, false);

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);
            }
        }));
    }
}
