package com.yaroslavlancelot.eafall.game.client.thick.server;

import com.badlogic.gdx.physics.box2d.Contact;
import com.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.yaroslavlancelot.eafall.game.entity.ContactListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IHealthListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IVelocityListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.network.server.GameSocketServer;
import com.yaroslavlancelot.eafall.network.server.callbacks.InGameServer;
import com.yaroslavlancelot.eafall.network.server.messages.BuildingCreatedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.BuildingUpgradedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.GameObjectHealthChangedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.GameStartedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.MoneyChangedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.UnitCreatedServerMessage;
import com.yaroslavlancelot.eafall.network.server.messages.UnitFireServerMessage;

import org.andengine.engine.options.EngineOptions;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Server game. Extends {@link com.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity}
 * with adding logic of handling client operation (messages from client) and sending message about server
 * operations to client.
 */
public class ServerGameActivity extends ThickClientGameActivity implements InGameServer, IVelocityListener,
        IHealthListener, IFireListener {
    private GameSocketServer mGameSocketServer;

    private boolean mServerGameLoaded;
    private boolean mClientGameLoaded;

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        mServerGameLoaded = true;
        if (mClientGameLoaded) {
            startTheGame();
        }
    }

    @Override
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, IPlayer player, long... uniquesId) {
        PlanetStaticObject planetStaticObject = super.createPlanet(x, y, textureRegion, key, player, uniquesId);
        planetStaticObject.setGameObjectHealthChangedListener(this);
        return planetStaticObject;
    }

    @Override
    protected void userWantUpgradeBuilding(final IPlayer userPlayer, final BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            boolean isBuildingUpgraded = planetStaticObject.getBuilding(buildingId.getId())
                    .upgradeBuilding();
            if (isBuildingUpgraded) {
                mGameSocketServer.sendBroadcastServerMessage(0, new BuildingUpgradedServerMessage(
                        buildingId.getId(), buildingId.getUpgrade(), userPlayer.getName()));
            }
        }
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            boolean isBuildingCreated = userPlayer.getPlanet().createBuilding(buildingId);
            if (isBuildingCreated) {
                mGameSocketServer.sendBroadcastServerMessage(0, new BuildingCreatedServerMessage(
                        buildingId.getId(), buildingId.getUpgrade(), userPlayer.getName()));
            }
        }
    }

    @Override
    public OffenceUnit createMovableUnit(IPlayer unitPlayer, int unitKey, int x, int y, boolean isTopPath) {
        OffenceUnit unit = super.createMovableUnit(unitPlayer, unitKey, x, y, isTopPath);
        unit.setVelocityChangedListener(this);
        return unit;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mServerGameLoaded = false;
        mClientGameLoaded = false;
        mGameSocketServer = GameSocketServer.getGameSocketServer();
        mGameSocketServer.addInGameCallback(ServerGameActivity.this);
        return super.onCreateEngineOptions();
    }

    @Override
    protected Unit createUnit(int unitKey, IPlayer unitPlayer, float x, float y) {
        Unit unit = super.createUnit(unitKey, unitPlayer, x, y);
        mGameSocketServer.sendBroadcastServerMessage(0, new UnitCreatedServerMessage(unitPlayer.getName(), unitKey, unit));
        unit.setGameObjectHealthChangedListener(this);
        unit.setUnitFireCallback(this);
        return unit;
    }

    @Override
    public void velocityChanged(final GameObject unit) {
        if (unit instanceof OffenceUnit)
            mGameSocketServer.sendBroadcastServerMessage(0, new UnitChangePositionServerMessage((OffenceUnit) unit));
    }

    @Override
    public void gameObjectHealthChanged(long unitUniqueId, int newUnitHealth) {
        mGameSocketServer.sendBroadcastServerMessage(0, new GameObjectHealthChangedServerMessage(unitUniqueId, newUnitHealth));
    }

    @Override
    public void fire(long gameObjectUniqueId, long attackedGameObjectUniqueId) {
        mGameSocketServer.sendBroadcastServerMessage(0, new UnitFireServerMessage(gameObjectUniqueId, attackedGameObjectUniqueId));
    }

    @Override
    public void gameLoaded() {
        mClientGameLoaded = true;
        if (mServerGameLoaded) {
            startTheGame();
        }
        initMoney();
    }

    private void startTheGame() {
        mGameSocketServer.sendBroadcastServerMessage(0, new GameStartedServerMessage());
        hideSplash();
        registerContactCallback();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void registerContactCallback() {
        mContactListener.setContactCallback(new ContactListener.ContactCallback() {
            @Override
            public void contact(Contact contact) {
                resolveContactData(contact);
            }
        });
    }

    private void resolveContactData(final Contact contact) {
        Object userData = contact.getFixtureA().getBody().getUserData();
        if (userData != null) {
            if (userData instanceof OffenceUnit)
                velocityChanged((OffenceUnit) userData);
        }
        userData = contact.getFixtureB().getBody().getUserData();
        if (userData != null) {
            if (userData instanceof OffenceUnit)
                velocityChanged((OffenceUnit) userData);
        }
    }

    private void initMoney() {
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            final String key = player.getOxygenChangedKey();
            SharedEvents.addCallback(new SharedEvents.DataChangedCallback(key) {
                @Override
                public void callback(String callbackKey, Object value) {
                    mGameSocketServer.sendBroadcastServerMessage(0, new MoneyChangedServerMessage(
                            player.getName(), (Integer) value));
                }
            });
        }
    }
}
