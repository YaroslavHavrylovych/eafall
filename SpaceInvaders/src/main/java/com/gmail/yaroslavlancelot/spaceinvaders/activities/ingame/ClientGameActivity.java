package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGameClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.engine.options.EngineOptions;

import java.io.IOException;

/** Used in client. Handles messages from server and send it's own in react on client operations */
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
    protected void userWantCreateBuilding(final ITeam userTeam, BuildingId buildingId) {
        LoggerHelper.methodInvocation(TAG, "userWantCreateBuilding");
        try {
            mGameServerConnector.sendClientMessage(new BuildingCreationClientMessage(
                    userTeam.getTeamName(), buildingId.getId(), buildingId.getUpgrade()));
            LoggerHelper.printInformationMessage(TAG, "send building request team= " + userTeam.getTeamName() + ", building=" + buildingId + "");
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, e.getMessage());
        }
    }

    @Override
    protected void initThickClient() {
        // it's thin client, so no actions
    }

    @Override
    public void buildingCreated(BuildingId buildingId, final String teamName) {
        LoggerHelper.methodInvocation(TAG, "buildingCreated");
        LoggerHelper.printDebugMessage(TAG, "buildingId=" + buildingId + ", teamName=" + teamName);
        PlanetStaticObject planetStaticObject = TeamsHolder.getInstance().getElement(teamName).getTeamPlanet();
        planetStaticObject.createBuilding(buildingId);
    }

    @Override
    public void unitCreated(final String teamName, final int unitId, final float x, final float y, final long unitUniqueId) {
        LoggerHelper.printDebugMessage(TAG, "unitCreated=" + unitUniqueId + "(" + x + "," + y + ")");
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                createThinUnit(unitId, TeamsHolder.getInstance().getElement(teamName), x, y, unitUniqueId);
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

    @Override
    public void moneyChanged(String teamName, int money) {
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        if (team == null) return;
        team.setMoney(money);
    }
}
