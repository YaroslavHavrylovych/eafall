package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGame;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Server game. Extends physical world and will add some handlers for server actions and
 * from client.
 */
public class ServerGameActivity extends PhysicWorldGameActivity implements InGame {
    private GameSocketServer mGameSocketServer;

    @Override
    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                initPhysicWorld();

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);
            }
        }));
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameSocketServer = GameSocketServer.getGameSocketServer();
                mGameSocketServer.addInGameCallbacks(ServerGameActivity.this);
            }
        });
        return super.onCreateEngineOptions();
    }

    @Override
    public void newBuildingCreate(int buildingId) {
        if (mBlueTeam != null && mBlueTeam.getTeamPlanet() != null)
            mBlueTeam.getTeamPlanet().createBuildingById(buildingId);
    }
}
