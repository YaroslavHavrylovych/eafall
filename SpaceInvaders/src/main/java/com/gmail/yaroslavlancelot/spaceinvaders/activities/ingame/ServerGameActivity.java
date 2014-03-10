package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.InGameServer;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.options.EngineOptions;

import java.io.IOException;

/**
 * Server game. Extends physical world and will add some handlers for server actions and
 * from client.
 */
public class ServerGameActivity extends PhysicWorldGameActivity implements InGameServer {
    private GameSocketServer mGameSocketServer;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mGameSocketServer = GameSocketServer.getGameSocketServer();
        mGameSocketServer.addInGameCallbacks(ServerGameActivity.this);
        return super.onCreateEngineOptions();
    }

    @Override
    public void newBuildingCreate(int buildingId) {
        LoggerHelper.methodInvocation(TAG, "newBuildingCreate");
        if (mRedTeam != null && mRedTeam.getTeamPlanet() != null) {
            userWantCreateBuilding(mRedTeam, buildingId);
        }
    }

    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, final int buildingId) {
        LoggerHelper.printInformationMessage(TAG, "user want to create building with id=" + buildingId);
        PlanetStaticObject planetStaticObject = userTeam.getTeamPlanet();
        if (planetStaticObject != null) {
            boolean isBuildingCreated = userTeam.getTeamPlanet().purchaseBuilding(buildingId);
            LoggerHelper.printDebugMessage(TAG, "isBuildingCreated=" + isBuildingCreated);
            if (isBuildingCreated) {
                try {
                    mGameSocketServer.sendBroadcastServerMessage(new BuildingCreatedServerMessage(buildingId, userTeam.getTeamName()));
                } catch (IOException e) {
                    LoggerHelper.printErrorMessage(TAG, "send message (create building on client) IOException");
                }
            }
        }
    }
}
