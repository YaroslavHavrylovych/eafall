package com.yaroslavlancelot.eafall.game.client.thick.single;

import com.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

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
    protected void userWantUpgradeBuilding(final IPlayer userPlayer, final BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            planetStaticObject.getBuilding(buildingId.getId()).upgradeBuilding();
        }
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            planetStaticObject.createBuilding(buildingId);
        }
    }

    protected void initBotControlledPlayer(final IPlayer initializingPlayer) {
        Thread thread = new Thread(new VeryFirstBot(initializingPlayer));
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        mBotThread = thread;
        mBotThread.start();
    }
}
