package com.gmail.yaroslavlancelot.eafall.game.scene.scenes;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundOperationsImpl;
import com.gmail.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.GameSceneHandler;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Game scene
 * <br/>
 * 1. Contains game background and init touches.
 * <br/>
 * 2. Loading needed images.
 *
 * @author Yaroslav Havrylovych
 */
public class EaFallScene extends Scene {
    private static final String TAG = EaFallScene.class.getCanonicalName();
    private GameSceneHandler mGameSceneHandler;
    private Sprite mBackgroundSprite;

    public EaFallScene() {
    }

    public ICameraHandler getCameraHandler() {
        return mGameSceneHandler;
    }

    /** set background image to the scene */
    public void setBackground(String backgroundFilePath, VertexBufferObjectManager vertexBufferObjectManager) {
        AutoParallaxBackground background = new AutoParallaxBackground(0, 0, 0, 20);
        background.setParallaxValue(new Random().nextInt(SizeConstants.GAME_FIELD_WIDTH));
        mBackgroundSprite = new Sprite(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getInstance().getElement(backgroundFilePath),
                vertexBufferObjectManager);
        background.attachParallaxEntity(
                new ParallaxBackground.ParallaxEntity(1, mBackgroundSprite));
        setBackground(background);
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
    public void initGameSceneHandler(EaFallCamera camera) {
        LoggerHelper.methodInvocation(TAG, "initGameSceneHandler");
        /* main scene touch listener */
        mGameSceneHandler = new GameSceneHandler(camera) {
            @Override
            public void setZoomFactor(float zoomFactor) {
                super.setZoomFactor(zoomFactor);
                mBackgroundSprite.setScale(zoomFactor - (zoomFactor - 1) / 2.7f);
            }
        };
        setOnSceneTouchListener(mGameSceneHandler);
    }
}
