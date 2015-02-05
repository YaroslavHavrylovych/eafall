package com.gmail.yaroslavlancelot.eafall.game.client.thick.server;

import com.badlogic.gdx.physics.box2d.Contact;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.entity.ContactListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IHealthListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IVelocityListener;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.money.MoneyUpdatedEvent;
import com.gmail.yaroslavlancelot.eafall.network.server.GameSocketServer;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.GameObjectHealthChangedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.GameStartedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.MoneyChangedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitCreatedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitFireServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.callbacks.InGameServer;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.engine.options.EngineOptions;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.io.IOException;

/**
 * Server game. Extends {@link com.gmail.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity}
 * with adding logic of handling client operation (messages from client) and sending message about server
 * operations to client.
 */
public class ServerGameActivity extends ThickClientGameActivity implements InGameServer, IVelocityListener,
        IHealthListener, IFireListener {
    private GameSocketServer mGameSocketServer;

    private boolean mServerGameLoaded;
    private boolean mClientGameLoaded;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mServerGameLoaded = false;
        mClientGameLoaded = false;
        mGameSocketServer = GameSocketServer.getGameSocketServer();
        mGameSocketServer.addInGameCallback(ServerGameActivity.this);
        return super.onCreateEngineOptions();
    }

    @Override
    public void afterGameLoaded() {
        mServerGameLoaded = true;
        if (mClientGameLoaded){
            try {
                mGameSocketServer.sendBroadcastServerMessage(new GameStartedServerMessage());
            } catch (IOException e) {
                LoggerHelper.printErrorMessage(TAG, "send message (create building on server) IOException");
            }
            replaceSplashSceneWithGameScene();
            registerContactCallback();
        }
    }

    @Override
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, long... uniquesId) {
        PlanetStaticObject planetStaticObject = super.createPlanet(x, y, textureRegion, key, team, uniquesId);
        planetStaticObject.setGameObjectHealthChangedListener(this);
        return planetStaticObject;
    }

    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, BuildingId buildingId) {
        LoggerHelper.printInformationMessage(TAG, "user want to create building with id=" + buildingId);
        PlanetStaticObject planetStaticObject = userTeam.getTeamPlanet();
        if (planetStaticObject != null) {
            boolean isBuildingCreated = userTeam.getTeamPlanet().createBuilding(buildingId);
            LoggerHelper.printDebugMessage(TAG, "isBuildingCreated=" + isBuildingCreated);
            if (isBuildingCreated) {
                try {
                    mGameSocketServer.sendBroadcastServerMessage(new BuildingCreatedServerMessage(
                            buildingId.getId(), buildingId.getUpgrade(), userTeam.getTeamName()));
                } catch (IOException e) {
                    LoggerHelper.printErrorMessage(TAG, "send message (create building on server) IOException");
                }
            }
        }
    }

    @Override
    protected MovableUnit createMovableUnit(int unitKey, ITeam unitTeam, boolean isTopPath) {
        MovableUnit unit = super.createMovableUnit(unitKey, unitTeam, isTopPath);
        unit.setVelocityChangedListener(this);
        return unit;
    }

    @Override
    protected Unit createUnit(int unitKey, ITeam unitTeam, float x, float y) {
        Unit unit = super.createUnit(unitKey, unitTeam, x, y);

        try {
            mGameSocketServer.sendBroadcastServerMessage(new UnitCreatedServerMessage(unitTeam.getTeamName(), unitKey, unit));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (unit created on server) IOException");
        }
        unit.setGameObjectHealthChangedListener(this);
        unit.setUnitFireCallback(this);
        return unit;
    }

    @Override
    public void gameObjectHealthChanged(long unitUniqueId, int newUnitHealth) {
        try {
            mGameSocketServer.sendBroadcastServerMessage(new GameObjectHealthChangedServerMessage(unitUniqueId, newUnitHealth));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (game object health changed on server) IOException");
        }
    }

    @Override
    public void fire(long gameObjectUniqueId, long attackedGameObjectUniqueId) {
        try {
            mGameSocketServer.sendBroadcastServerMessage(new UnitFireServerMessage(gameObjectUniqueId, attackedGameObjectUniqueId));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (game object health changed on server) IOException");
        }
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
            if (userData instanceof MovableUnit)
                velocityChanged((MovableUnit) userData);
        }
        userData = contact.getFixtureB().getBody().getUserData();
        if (userData != null) {
            if (userData instanceof MovableUnit)
                velocityChanged((MovableUnit) userData);
        }
    }

    @Override
    public void velocityChanged(final GameObject unit) {
        try {
            if (unit instanceof MovableUnit)
                mGameSocketServer.sendBroadcastServerMessage(new UnitChangePositionServerMessage((MovableUnit) unit));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (unit moved on server) IOException");
        }
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(MoneyUpdatedEvent moneyUpdatedEvent) {
        TeamControlBehaviourType behaviourType =
                TeamsHolder.getInstance().getElement(moneyUpdatedEvent.getTeamName()).getTeamControlType();
        try {
            mGameSocketServer.sendBroadcastServerMessage(new MoneyChangedServerMessage(
                    moneyUpdatedEvent.getTeamName(), moneyUpdatedEvent.getMoney()));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (money changed) IOException");
        }
    }

    @Override
    public void gameLoaded() {
        mClientGameLoaded = true;
        if (mServerGameLoaded){
            try {
                mGameSocketServer.sendBroadcastServerMessage(new GameStartedServerMessage());
            } catch (IOException e) {
                LoggerHelper.printErrorMessage(TAG, "send message (create building on server) IOException");
            }
            replaceSplashSceneWithGameScene();
            registerContactCallback();
        }
    }
}
