package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IVelocityChangedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.InGameServer;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.options.EngineOptions;

import java.io.IOException;

/**
 * Server game. Extends physical world and will add some handlers for server actions and
 * from client.
 */
public class ServerGameActivity extends PhysicWorldGameActivity implements InGameServer, IVelocityChangedListener {
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
                    LoggerHelper.printErrorMessage(TAG, "send message (create building on server) IOException");
                }
            }
        }
    }

    @Override
    protected Unit createAndAttachUnitCarcass(final int unitKey, final ITeam unitTeam) {
        Unit unit = super.createAndAttachUnitCarcass(unitKey, unitTeam);
        try {
            mGameSocketServer.sendBroadcastServerMessage(new UnitCreatedServerMessage(unitTeam.getTeamName(), unitKey, unit));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "send message (unit created on server) IOException");
        }
        unit.setVelocityChangedListener(this);
        return unit;
    }

    @Override
    protected void initPhysicWorld() {
        super.initPhysicWorld();
        setContactListener(new ContactListener() {
            @Override
            public void beginContact(final Contact contact) {
            }

            @Override
            public void endContact(final Contact contact) {
                Object userData = contact.getFixtureA().getBody().getUserData();
                sendUnitPositionChanged(userData);
                userData = contact.getFixtureB().getBody().getUserData();
                sendUnitPositionChanged(userData);
            }

            @Override
            public void preSolve(final Contact contact, final Manifold oldManifold) {
            }

            @Override
            public void postSolve(final Contact contact, final ContactImpulse impulse) {
            }
        });
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

    private void sendUnitPositionChanged(Object userObject) {
        if (userObject == null || !(userObject instanceof Unit))
            return;

        velocityChanged((Unit) userObject);
    }
}
