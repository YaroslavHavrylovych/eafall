package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGameClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.engine.options.EngineOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;

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
    protected void initThickClient() {
        // it's thin client, so no actions
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

    @Override
    public void unitCreated(final String teamName, final int unitId, final float x, final float y, long unitUniqueId) {
        LoggerHelper.printDebugMessage(TAG, "unitCreated=" + unitUniqueId + "(" + x + "," + y + ")");
        if (unitId == 0)
            createUnit(unitId, mTeams.get(teamName), x, y, unitUniqueId);
    }

    @Override
    public void unitMoved(UnitChangePositionServerMessage unitChangePositionServerMessage) {
        long unitUniqueId = unitChangePositionServerMessage.getUnitUniqueId();
        float x = unitChangePositionServerMessage.getX(),
                y = unitChangePositionServerMessage.getY();
        LoggerHelper.printDebugMessage(TAG, "unitCreated=" + unitUniqueId + "(" + x + "," + y + ")");
        Unit unit = getUnitById(unitUniqueId);
        if (unit == null) {
            LoggerHelper.printInformationMessage(TAG, "try yo move uncreated unit");
            return;
        }
        final float widthD2 = unit.getWidth() / 2;
        final float heightD2 = unit.getHeight() / 2;
        unit.setBodyTransform((x + widthD2) / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT, (y + heightD2) / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
        unit.setUnitLinearVelocity(unitChangePositionServerMessage.getVelocityX(), unitChangePositionServerMessage.getVelocityY());
    }
}
