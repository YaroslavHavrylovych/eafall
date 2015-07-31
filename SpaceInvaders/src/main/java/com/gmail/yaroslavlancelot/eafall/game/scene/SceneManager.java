package com.gmail.yaroslavlancelot.eafall.game.scene;

import com.gmail.yaroslavlancelot.eafall.game.EaFallActivity;
import com.gmail.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;

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
    private EaFallActivity mEaFallActivity;

    /**
     * Constructs SceneManager using engine and game activity
     *
     * @param eaFallActivity any instance of game activity
     */
    public SceneManager(EaFallActivity eaFallActivity) {
        mEaFallActivity = eaFallActivity;
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
        return mSplashScene = new SplashScene(mEaFallActivity.getEngine());
    }

    /**
     * remove scenes and detaches all entities
     */
    public void clear() {
        clearSplashScene();
        clearWorkingScene();
    }

    /**
     * clear working scene and detaches all entities
     */
    public void clearWorkingScene() {
        if (mWorkingScene != null) {
            detachFromEntity(mWorkingScene);
            clearScene(mWorkingScene);
            mWorkingScene = null;
        }
    }

    /**
     * clear splash scene and detaches all entities
     */
    public void clearSplashScene() {
        if (mSplashScene != null) {
            detachFromEntity(mSplashScene);
            clearScene(mSplashScene);
            mSplashScene = null;
        }
    }

    /**
     * call native scene methods to detach everything
     *
     * @param scene {@link Scene} to work with
     */
    private void clearScene(Scene scene) {
        scene.clearChildScene();
        scene.detachChildren();
        scene.reset();
        scene.detachSelf();
    }

    /**
     * recursively detaches all entities children and the entity itself
     *
     * @param entity entity to detach
     */
    private void detachFromEntity(IEntity entity) {
        if (entity == null) {
            return;
        }
        for (int i = 0; i < entity.getChildCount(); i++) {
            detachFromEntity(entity.getFirstChild());
            entity.detachSelf();
        }
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
        mEaFallActivity.getEngine().setScene(mWorkingScene);
    }

    /**
     * showing splash scene
     */
    public void showSplash() {
        if (mSplashScene == null) {
            throw new IllegalStateException("mSplashScene have not been initialized");
        }
        mEaFallActivity.getEngine().setScene(mSplashScene);
    }
}
