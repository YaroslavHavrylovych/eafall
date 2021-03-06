package com.yaroslavlancelot.eafall.game.scene.scenes;

import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.audio.SoundOperationsImpl;
import com.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.touch.GameSceneHandler;
import com.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
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
public class EaFallScene extends Scene implements ClickDetector.IClickDetectorListener {
    private GameSceneHandler mGameSceneHandler;
    private boolean mParallax;
    private Sprite mBackgroundSprite;
    private AutoParallaxBackground mBackground;
    private ClickDetector mClickDetector;

    public EaFallScene(boolean parallax) {
        mParallax = parallax;
        mClickDetector = new ClickDetector(this);
    }

    public ICameraHandler getCameraHandler() {
        return mGameSceneHandler;
    }

    public float getMinZoomFactor() {
        return mGameSceneHandler.getMinZoomFactor();
    }

    public void setMinZoomFactor(float zoomFactor) {
        mGameSceneHandler.setMinZoomFactor(zoomFactor);
    }

    public void setSceneTouchSilentListener(IOnSceneTouchListener sceneTouchSilentListener) {
        mGameSceneHandler.setSceneTouchSilentListener(sceneTouchSilentListener);
    }

    public void setClickListener(ClickDetector.IClickDetectorListener clickListener) {
        mGameSceneHandler.setClickListener(clickListener);
    }

    /** set background image to the scene */
    public void setBackground(String backgroundFilePath, VertexBufferObjectManager vertexBufferObjectManager) {
        mBackgroundSprite = new Sprite(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                TextureRegionHolder.getInstance().getElement(backgroundFilePath),
                vertexBufferObjectManager);
        setTouchAreaBindingOnActionMoveEnabled(true);
        setOnAreaTouchTraversalFrontToBack();
        AutoParallaxBackground background = new AutoParallaxBackground(0, 0, 0, 0);
        background.setParallaxValue(new Random().nextInt(SizeConstants.GAME_FIELD_WIDTH));
        background.attachParallaxEntity(
                new ParallaxBackground.ParallaxEntity(1, mBackgroundSprite));
        setBackground(background);
        if (mParallax) {
            background.setParallaxChangePerSecond(20);
        }
        mBackground = background;
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        mClickDetector.onTouchEvent(pSceneTouchEvent);
        return super.onSceneTouchEvent(pSceneTouchEvent);
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
        /* main scene touch listener */
        mGameSceneHandler = new GameSceneHandler(camera) {
            private float mPreviousZoomFactor = mMinZoomFactor;

            @Override
            public void cameraMove(final float deltaX, final float deltaY) {
                super.cameraMove(deltaX, deltaY);

                if (smoothZoomInProgress()) {
                    float zoomFactor = getZoomFactor();
                    if (mPreviousZoomFactor - zoomFactor != 0.0f) {
                        mPreviousZoomFactor = zoomFactor;
                        mBackgroundSprite.setScale(zoomFactor - (zoomFactor - 1) / 2f);
                    }
                }

                if (deltaX != 0.0) {
                    mBackground.changeParallax(-deltaX * mBackgroundSprite.getScaleY() / getZoomFactor());
                }

                if (deltaY != 0.0) {
                    float multiplier = Math.min(0.7f, mBackgroundSprite.getScaleY() /
                            getZoomFactor());
                    float delta = SizeConstants.HALF_FIELD_HEIGHT - getCenterY();
                    mBackgroundSprite.setY(SizeConstants.HALF_FIELD_HEIGHT + delta * multiplier);
                }
            }
        };
        setOnSceneTouchListener(mGameSceneHandler);
    }

    @Override
    public void onClick(ClickDetector pClickDetector, int pPointerID, float pSceneX, float pSceneY) {
        SoundFactory.getInstance().playSound(StringConstants.SOUND_TAP_ON_SCREEN_PATH);
    }
}
