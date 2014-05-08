package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGameClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

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
    public void unitCreated(final String teamName, final int unitId, final float x, final float y, final long unitUniqueId) {
        LoggerHelper.printDebugMessage(TAG, "unitCreated=" + unitUniqueId + "(" + x + "," + y + ")");
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                createThinUnit(unitId, mTeams.get(teamName), x, y, unitUniqueId);
            }
        });
    }

    @Override
    public void unitMoved(final UnitChangePositionServerMessage unitChangePositionServerMessage) {
        long unitUniqueId = unitChangePositionServerMessage.getUnitUniqueId();
        final float x = unitChangePositionServerMessage.getX(),
                y = unitChangePositionServerMessage.getY();
        final float velocityX = unitChangePositionServerMessage.getVelocityX(),
                velocityY = unitChangePositionServerMessage.getVelocityY();
        final float rotation = unitChangePositionServerMessage.getRotationAngle();
        LoggerHelper.printDebugMessage(TAG, "unitMoved=" + unitUniqueId + "(" + x + "," + y + "), vel(" +
                +velocityX + "," + velocityY + "), rotation=" + rotation);
        final GameObject gameObject = getGameObjectById(unitUniqueId);

        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (gameObject == null || !(gameObject instanceof Unit)) {
                    LoggerHelper.printInformationMessage(TAG, "try yo move uncreated unit or it's not a unit");
                    return;
                }
                Unit unit = (Unit) gameObject;
                if (!gameObject.isObjectAlive()) return;
                unit.setUnitPosition(x, y);
                unit.rotate(rotation);
                unit.setUnitLinearVelocity(velocityX, velocityY);
            }
        });
    }

    @Override
    public void gameObjectHealthChanged(long gameObjectUniqueId, final int newUnitHealth) {
        final GameObject gameObject = getGameObjectById(gameObjectUniqueId);
        if (gameObject == null) {
            LoggerHelper.printInformationMessage(TAG, "try to change health of unexisting unit");
            return;
        }
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                gameObject.setHealth(newUnitHealth);
            }
        });
    }

    @Override
    public void unitFire(long gameObjectUniqueId, long attackedGameObjectUniqueId) {
        final GameObject gameObject = getGameObjectById(gameObjectUniqueId);
        final GameObject objectToAttack = getGameObjectById(attackedGameObjectUniqueId);
        if (gameObject == null || objectToAttack == null) {
            LoggerHelper.printErrorMessage(TAG, "one of the object in attack is not exist");
            return;
        }
        if (!(gameObject instanceof Unit)) {
            LoggerHelper.printErrorMessage(TAG, "attacker is not unit in fireFromPosition operation");
            return;
        }
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Unit unit = (Unit) gameObject;
                if (unit.isObjectAlive())
                    unit.fire(objectToAttack);
            }
        });
    }
}
