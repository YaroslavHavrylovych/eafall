package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGame;
import org.andengine.engine.options.EngineOptions;

/**
 * Server game. Extends physical world and will add some handlers for server actions and
 * from client.
 */
public class ServerGameActivity extends PhysicWorldGameActivity implements InGame {
    private GameSocketServer mGameSocketServer;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mGameSocketServer = GameSocketServer.getGameSocketServer();
        mGameSocketServer.addInGameCallbacks(this);
        return super.onCreateEngineOptions();
    }

    @Override

    public void newBuildingCreate(int buildingId) {
        if (mBlueTeam != null && mBlueTeam.getTeamPlanet() != null)
            mBlueTeam.getTeamPlanet().createBuildingById(buildingId);
    }
}
