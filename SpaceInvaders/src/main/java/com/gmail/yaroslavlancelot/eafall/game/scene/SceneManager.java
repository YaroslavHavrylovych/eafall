package com.gmail.yaroslavlancelot.eafall.game.scene;

import com.gmail.yaroslavlancelot.eafall.game.GameActivity;
import com.gmail.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;

/**
 * Manager class for scenes
 * Creates and stores and changes scenes
 *
 * @author Yaroslav Havrylovych
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
     * Returns stored SplashScene instance
     *
     * @return instance of SplashScene, if not created yet - got null;
     */
    public SplashScene getSplashScene() {
        return mSplashScene;
    }

    /**
     * Returns stored EaFallScene instance
     *
     * @return instance of EaFallScene, if not created yet - got null;
     */
    public synchronized EaFallScene getWorkingScene() {
        return mWorkingScene;
    }

    /**
     * Creates and stores splash scene
     *
     * @return instance of SplashScene
     */
    public SplashScene initSplashScene() {
        return mSplashScene = new SplashScene(mGameActivity.getEngine());
    }

    /**
     * Creates and stores game scene
     *
     * @return instance of SplashScene
     */
    public synchronized EaFallScene initWorkingScene(EaFallCamera camera, boolean parallax) {
        mWorkingScene = new EaFallScene(parallax);
        mWorkingScene.initGameSceneHandler(camera);
        return mWorkingScene;
    }

    /**
     * just invocation of {@link SceneManager#initWorkingScene(EaFallCamera, boolean)}
     * with true as boolean param
     */
    public synchronized EaFallScene initWorkingScene(EaFallCamera camera) {
        return this.initWorkingScene(camera, true);
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
