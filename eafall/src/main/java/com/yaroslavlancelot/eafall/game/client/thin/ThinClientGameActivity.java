package com.yaroslavlancelot.eafall.game.client.thin;

import com.badlogic.gdx.physics.box2d.Body;
import com.yaroslavlancelot.eafall.game.client.ClientGameActivity;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.network.client.callbacks.InGameClient;
import com.yaroslavlancelot.eafall.network.client.connector.GameServerConnector;
import com.yaroslavlancelot.eafall.network.client.messages.BuildingCreationClientMessage;
import com.yaroslavlancelot.eafall.network.client.messages.GameLoadedClientMessage;
import com.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;

import org.andengine.engine.options.EngineOptions;

/** Used in client. Handles messages from server and send it's own in react on client operations */
public class ThinClientGameActivity extends ClientGameActivity implements InGameClient {
    public final static String TAG = ThinClientGameActivity.class.getCanonicalName();
    private volatile GameServerConnector mGameServerConnector;

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        mGameServerConnector.sendClientMessage(0, new GameLoadedClientMessage());
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mGameServerConnector = GameServerConnector.getGameServerConnector();
        mGameServerConnector.addInGameCallback(this);
        return super.onCreateEngineOptions();
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        mGameServerConnector.sendClientMessage(0, new BuildingCreationClientMessage(
                userPlayer.getName(), buildingId.getId(), buildingId.getUpgrade()));
    }

    @Override
    public void buildingCreated(BuildingId buildingId, final String playerName) {
        PlanetStaticObject planetStaticObject = PlayersHolder.getPlayer(playerName).getPlanet();
        planetStaticObject.createBuilding(buildingId);
    }

    @Override
    public void unitCreated(final String playerName, final int unitId, final float x, final float y, final long unitUniqueId) {
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                createThinUnit(unitId, PlayersHolder.getPlayer(playerName), x, y, unitUniqueId);
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
        final GameObject gameObject = getGameObjectById(unitUniqueId);

        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (gameObject == null || !(gameObject instanceof OffenceUnit)) {
                    return;
                }
                OffenceUnit unit = (OffenceUnit) gameObject;
                if (!gameObject.isObjectAlive()) return;
                Body body = unit.getBody();
                body.setTransform(x, y, 0);
                unit.setRotation(rotation);
                unit.setUnitLinearVelocity(velocityX, velocityY);
            }
        });
    }

    @Override
    public void gameObjectHealthChanged(long gameObjectUniqueId, final int newUnitHealth) {
        final GameObject gameObject = getGameObjectById(gameObjectUniqueId);
        if (gameObject == null) {
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
            return;
        }
        if (!(gameObject instanceof Unit)) {
            return;
        }
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Unit unit = (Unit) gameObject;
                if (unit.isObjectAlive()) {
                    //TODO this probably wouldn't work (rotation logic was changed)
                    unit.fire(objectToAttack);
                }
            }
        });
    }

    @Override
    public void moneyChanged(String playerName, int money) {
        IPlayer player = PlayersHolder.getPlayer(playerName);
        if (player == null) return;
        player.setMoney(money);
    }

    @Override
    public void gameStarted() {
        hideSplash();
    }
}
