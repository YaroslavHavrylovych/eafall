package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGameClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;

import java.io.IOException;

public class ClientGameActivity extends MainOperationsBaseGameActivity implements InGameClient {
    public final static String TAG = ClientGameActivity.class.getCanonicalName();
    private volatile GameServerConnector mGameServerConnector;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mGameServerConnector = GameServerConnector.getGameServerConnector();
        mGameServerConnector.addInGameCallback(this);
        return super.onCreateEngineOptions();
    }

    @Override
    protected void initPhysicWorld() {
        // no physic world at client side
    }

    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        //no physic body at client
    }

    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, final int buildingId) {
        LoggerHelper.methodInvocation(TAG, "userWantCreateBuilding");
        try {
            mGameServerConnector.sendClientMessage(new BuildingCreationClientMessage(buildingId));
            LoggerHelper.printInformationMessage(TAG, "send message with building=" + buildingId + " creation request");
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, e.getMessage());
        }
    }

    @Override
    public void buildingCreated(final int buildingId, final String teamName) {
        LoggerHelper.methodInvocation(TAG, "buildingCreated");
        LoggerHelper.printDebugMessage(TAG, "buildingId=" + buildingId + ", teamName=" + teamName);
        PlanetStaticObject planetStaticObject = mTeams.get(teamName).getTeamPlanet();
        planetStaticObject.createBuildingById(buildingId);
    }
}
