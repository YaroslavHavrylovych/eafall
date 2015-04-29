package com.gmail.yaroslavlancelot.eafall.game.scene.scenes;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundOperationsImpl;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.GameSceneHandler;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.engine.camera.VelocityCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Game scene
 * <br/>
 * 1. Contains game background and init touches.
 * <br/>
 * 2. Loading needed images.
 */
public class EaFallScene extends Scene {
    private static final String TAG = EaFallScene.class.getCanonicalName();
    private GameSceneHandler mGameSceneHandler;

    public EaFallScene() {
    }

    /** set background image to the scene */
    public void setBackground(String backgroundFilePath, VertexBufferObjectManager vertexBufferObjectManager) {
        setBackground(new SpriteBackground(
                new Sprite(
                        SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                        TextureRegionHolder.getInstance().getElement(backgroundFilePath),
                        vertexBufferObjectManager)));
    }

    public ICameraHandler getCameraHandler() {
        return mGameSceneHandler;
    }

    /**
     * Creates and init {@link GameSceneHandler}
     * and assign it to the {@link EaFallScene} instance.
     * <br/>
     * Set camera coordinates to music and sound handler with using
     * {@link SoundOperationsImpl#setCameraHandler(ICameraHandler)}
     *
     * @param camera camera to pass to the scene touch listener
     */
    public void initGameSceneHandler(VelocityCamera camera) {
        LoggerHelper.methodInvocation(TAG, "initGameSceneHandler");
        /* main scene touch listener */
        mGameSceneHandler = new GameSceneHandler(camera);
        setOnSceneTouchListener(mGameSceneHandler);
    }
}
