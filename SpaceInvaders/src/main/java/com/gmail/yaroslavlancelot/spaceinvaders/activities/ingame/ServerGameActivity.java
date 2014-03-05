package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGame;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
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
        mGameSocketServer.addInGameCallbacks(ServerGameActivity.this);
        return super.onCreateEngineOptions();
    }

    @Override
    public void newBuildingCreate(int buildingId) {
        if (mBlueTeam != null && mBlueTeam.getTeamPlanet() != null)
            mBlueTeam.getTeamPlanet().purchaseBuilding(buildingId);
    }

    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, final int buildingId) {
        PlanetStaticObject planetStaticObject = userTeam.getTeamPlanet();
        if (planetStaticObject != null)
            userTeam.getTeamPlanet().createBuildingById(buildingId);
    }
}
