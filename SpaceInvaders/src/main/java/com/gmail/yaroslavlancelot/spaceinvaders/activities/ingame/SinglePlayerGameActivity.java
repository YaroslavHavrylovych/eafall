package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGame;
import org.andengine.engine.options.EngineOptions;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends PhysicWorldGameActivity implements InGame {
    private GameSocketServer mGameSocketServer;

    @Override
    public EngineOptions onCreateEngineOptions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameSocketServer = GameSocketServer.getGameSocketServer();
                mGameSocketServer.addInGameCallbacks(SinglePlayerGameActivity.this);
            }
        });
        return super.onCreateEngineOptions();
    }

    @Override
    public void newBuildingCreate(int buildingId) {
        if (mBlueTeam != null && mBlueTeam.getTeamPlanet() != null)
            mBlueTeam.getTeamPlanet().createBuildingById(buildingId);
    }
}
