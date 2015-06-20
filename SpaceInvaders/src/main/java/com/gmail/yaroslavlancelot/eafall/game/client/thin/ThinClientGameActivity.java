package com.gmail.yaroslavlancelot.eafall.game.client.thin;

import com.badlogic.gdx.physics.box2d.Body;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.client.ClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.network.client.callbacks.InGameClient;
import com.gmail.yaroslavlancelot.eafall.network.client.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.GameLoadedClientMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;

import org.andengine.engine.options.EngineOptions;

/** Used in client. Handles messages from server and send it's own in react on client operations */
public class ThinClientGameActivity extends ClientGameActivity implements InGameClient {
    public final static String TAG = ThinClientGameActivity.class.getCanonicalName();
    private volatile GameServerConnector mGameServerConnector;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mGameServerConnector = GameServerConnector.getGameServerConnector();
        mGameServerConnector.addInGameCallback(this);
        return super.onCreateEngineOptions();
    }

    @Override
    public void onResourcesLoaded() {
        LoggerHelper.methodInvocation(TAG, "onResourcesLoaded");
        mGameServerConnector.sendClientMessage(0, new GameLoadedClientMessage());
        LoggerHelper.printInformationMessage(TAG, "send gameLoaded");
    }

    @Override
    protected void initThickClient() {
        // it's thin client, so no actions
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        LoggerHelper.methodInvocation(TAG, "userWantCreateBuilding");
        mGameServerConnector.sendClientMessage(0, new BuildingCreationClientMessage(
                userPlayer.getName(), buildingId.getId(), buildingId.getUpgrade()));
        LoggerHelper.printInformationMessage(TAG, "send building request player= " + userPlayer.getName() + ", building=" + buildingId + "");
    }

    @Override
    public void buildingCreated(BuildingId buildingId, final String playerName) {
        LoggerHelper.methodInvocation(TAG, "buildingCreated");
        LoggerHelper.printDebugMessage(TAG, "buildingId=" + buildingId + ", playerName=" + playerName);
        PlanetStaticObject planetStaticObject = PlayersHolder.getInstance().getElement(playerName).getPlanet();
        planetStaticObject.createBuilding(buildingId);
    }

    @Override
    public void unitCreated(final String playerName, final int unitId, final float x, final float y, final long unitUniqueId) {
        LoggerHelper.printDebugMessage(TAG, "unitCreated=" + unitUniqueId + "(" + x + "," + y + ")");
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                createThinUnit(unitId, PlayersHolder.getInstance().getElement(playerName), x, y, unitUniqueId);
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
                if (gameObject == null || !(gameObject instanceof MovableUnit)) {
                    LoggerHelper.printInformationMessage(TAG, "try yo move uncreated unit or it's not a unit");
                    return;
                }
                MovableUnit unit = (MovableUnit) gameObject;
                if (!gameObject.isObjectAlive()) return;
                Body body = unit.getBody();
                body.setTransform(x, y, 0);
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
            LoggerHelper.printErrorMessage(TAG, "attacker is not unit in fire operation");
            return;
        }
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Unit unit = (Unit) gameObject;
                if (unit.isObjectAlive()) {
                    unit.fire(objectToAttack);
                }
            }
        });
    }

    @Override
    public void moneyChanged(String playerName, int money) {
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);
        if (player == null) return;
        player.setMoney(money);
    }

    @Override
    public void gameStarted() {
        hideSplash();
    }
}
