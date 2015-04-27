package com.gmail.yaroslavlancelot.eafall.game.scene;

import com.gmail.yaroslavlancelot.eafall.game.GameActivity;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;

import org.andengine.engine.camera.VelocityCamera;

/**
 * Manager class for scenes
 * Creates and stores and changes scenes
 */
public class SceneManager {

    /** tag, which is used for debugging purpose */
    public static final String TAG = SceneManager.class.getCanonicalName();

    /** splash scene */
    private SplashScene mSplashScene;
    /** game scene */
    private EaFallScene mWorkingScene;
    /** main game activity */
    private GameActivity mGameActivity;

    /**
     * Constructs SceneManager using engine and game activity
     *
     * @param gameActivity any instance of game activity
     */
    public SceneManager(GameActivity gameActivity) {
        mGameActivity = gameActivity;
    }

    /**
     * Creates and stores splash scene
     *
     * @return instance of SplashScene
     */
    public SplashScene createSplashScene() {
        return mSplashScene = new SplashScene(mGameActivity.getEngine());
    }

    /**
     * Returns stored SplashScene instance
     *
     * @return instance of SplashScene, if not created yet - got null;
     */
    public SplashScene getSplashScene() {
        return mSplashScene;
    }

    /**
     * Creates and stores game scene
     *
     * @return instance of SplashScene
     */
    public EaFallScene createGameScene(VelocityCamera camera) {
        mWorkingScene = new EaFallScene(mGameActivity.getVertexBufferObjectManager());
        mWorkingScene.initGameSceneHandler(camera);
        return mWorkingScene;
    }

    /**
     * Returns stored EaFallScene instance
     *
     * @return instance of EaFallScene, if not created yet - got null;
     */
    public EaFallScene getWorkingScene() {
        return mWorkingScene;
    }

    /**
     * Replaces splash scene with game scene
     *
     * @throws IllegalStateException if splash scene or game scene have not been created yet
     */
    public void hideSplash() {
        if (mSplashScene == null || mWorkingScene == null) {
            throw new IllegalStateException("mSplashScene or mWorkingScene have not been initialized");
        }
        mSplashScene.detachSelf();
        mGameActivity.getEngine().setScene(mWorkingScene);
    }
}
