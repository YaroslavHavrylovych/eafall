package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

public class ClientGameActivity extends MainOperationsBaseGameActivity {

    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        //no physic body at client
    }

    @Override
    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                onLoadGameResources();

                onInitGameScene();
                onInitPlanetsSetEnemies();

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);
            }
        }));
    }
}
