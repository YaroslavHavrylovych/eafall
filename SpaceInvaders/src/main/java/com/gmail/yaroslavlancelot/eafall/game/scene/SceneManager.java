package com.gmail.yaroslavlancelot.eafall.game.scene;

import com.gmail.yaroslavlancelot.eafall.game.client.MainOperationsBaseGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.GameScene;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;

import org.andengine.engine.camera.SmoothCamera;

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
    private GameScene mGameScene;
    /** main game activity */
    private MainOperationsBaseGameActivity mGameActivity;

    /**
     * Constructs SceneManager using engine and game activity
     *
     * @param gameActivity      any instance of game activity
     */
    public SceneManager(MainOperationsBaseGameActivity gameActivity){
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
    public GameScene createGameScene(SmoothCamera smoothCamera) {
        //game scene
        mGameScene = new GameScene(mGameActivity.getVertexBufferObjectManager());
        mGameScene.initGameSceneTouch(
                mGameActivity.getWindowManager(), smoothCamera, mGameActivity.getmMusicAndSoundsHandler());
        return mGameScene;
    }

    /**
     * Returns stored GameScene instance
     *
     * @return instance of GameScene, if not created yet - got null;
     */
    public GameScene getGameScene() {
        return mGameScene;
    }

    /**
     * Replaces splash scene with game scene
     *
     * @throws IllegalStateException if splash scene or game scene have not been created yet
     */
    public void replaceSplashSceneWithGame() {
        if (mSplashScene == null || mGameScene == null){
            throw new IllegalStateException("mSplashScene or mGameScene have not been initialized");
        }
        mSplashScene.detachSelf();
        mGameActivity.getEngine().setScene(mGameScene);
    }
}
