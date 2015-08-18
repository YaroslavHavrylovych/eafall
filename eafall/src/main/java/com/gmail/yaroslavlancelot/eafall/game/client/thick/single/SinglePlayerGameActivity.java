package com.gmail.yaroslavlancelot.eafall.game.client.thick.single;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends ThickClientGameActivity {
    private Thread mBotThread;

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    @Override
    protected void initPlayer(final IPlayer player) {
        super.initPlayer(player);

        if (player.getControlType().bot()) {
            initBotControlledPlayer(player);
        }
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            userPlayer.getPlanet().createBuilding(buildingId);
        }
    }

    protected void initBotControlledPlayer(final IPlayer initializingPlayer) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledPlayer");
        Thread thread = new Thread(new VeryFirstBot(initializingPlayer));
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        mBotThread = thread;
        mBotThread.start();
    }
}
