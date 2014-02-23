package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

public class ClientGameActivity extends MainOperationsBaseGameActivity {
    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        //no physic body at client
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback onCreateSceneCallback) throws Exception {
        LoggerHelper.methodInvocation(TAG, "onCreateScene");

        onInitScene();
        onInitSceneObjects();

        onCreateSceneCallback.onCreateSceneFinished(mScene);
    }
}
