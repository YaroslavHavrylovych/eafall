package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IGameObjectHealthChanged;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IUnitFireCallback;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IVelocityChangedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.GameObjectHealthChangedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.MoneyChangedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitFireServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.InGameServer;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.engine.options.EngineOptions;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.io.IOException;

/**
 * Server game. Extends {@link com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.ThickClientGameActivity}
 * with adding logic of handling client operation (messages from client) and sending message about server
 * operations to client.
 */
public class ServerGameActivity extends ThickClientGameActivity implements InGameServer, IVelocityChangedListener,
        IGameObjectHealthChanged, IUnitFireCallback {
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
        if (mSecondTeam != null && mSecondTeam.getTeamPlanet() != null) {
            userWantCreateBuilding(mSecondTeam, buildingId);
        }
    }

    @Override
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, boolean isFakePlanet, long... uniquesId) {
        PlanetStaticObject planetStaticObject = super.createPlanet(x, y, textureRegion, key, team, isFakePlanet, uniquesId);
        planetStaticObject.setGameObjectHealthChangedListener(this);
        return planetStaticObject;
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
                    LoggerHelper.printErrorMessage(TAG, "send message (create building on server) IOException");
                }
            }
        }
    }

    @Override
    protected Unit createThinUnit(int unitKey, ITeam unitTeam, float x, float y, long... unitUniqueId) {
        Unit unit = super.createThinUnit(unitKey, unitTeam, x, y);
        try {
            mGameSocketServer.sendBroadcastServerMessage(new UnitCreatedServerMessage(unitTeam.getTeamName(), unitKey, unit));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (unit created on server) IOException");
        }
        unit.setGameObjectHealthChangedListener(this);
        unit.setVelocityChangedListener(this);
        unit.setUnitFireCallback(this);
        return unit;
    }

    private void resolveContactData(final Contact contact) {
        Object userData = contact.getFixtureA().getBody().getUserData();
        if (userData != null) {
            if (userData instanceof Unit)
                velocityChanged((Unit) userData);
        }
        userData = contact.getFixtureB().getBody().getUserData();
        if (userData != null) {
            if (userData instanceof Unit)
                velocityChanged((Unit) userData);
        }
    }

    @Override
    public void velocityChanged(final GameObject unit) {
        try {
            if (unit instanceof Unit)
                mGameSocketServer.sendBroadcastServerMessage(new UnitChangePositionServerMessage((Unit) unit));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (unit moved on server) IOException");
        }
    }

    @Override
    protected ITeam createTeam(String teamNameInExtra, IRace race) {
        final ITeam team = super.createTeam(teamNameInExtra, race);
        if ((team instanceof Team) && (team.getTeamControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_SERVER_SIDE)) {
            ((Team) team).setMoneyChangedCallback(new Team.IMoneyChangedCallback() {
                @Override
                public void moneyChanged(int delta) {
                    try {
                        mGameSocketServer.sendBroadcastServerMessage(
                                new MoneyChangedServerMessage(team.getTeamName(), team.getMoney()));
                    } catch (IOException e) {
                        LoggerHelper.printErrorMessage(TAG, "send message (money changed) IOException");
                    }
                }
            });
        }
        return team;
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

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        super.preSolve(contact, oldManifold);
        resolveContactData(contact);
    }

    @Override
    public void endContact(Contact contact) {
        super.endContact(contact);
        resolveContactData(contact);
    }

    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        resolveContactData(contact);
    }
}