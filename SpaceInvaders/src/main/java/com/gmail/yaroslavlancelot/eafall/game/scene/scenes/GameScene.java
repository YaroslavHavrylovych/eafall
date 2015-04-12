package com.gmail.yaroslavlancelot.eafall.game.scene.scenes;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.sound.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.eafall.game.touch.GameSceneTouchListener;

import org.andengine.engine.camera.SmoothCamera;
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
public class GameScene extends Scene {
    private static final String TAG = GameScene.class.getCanonicalName();

    /** set background image to the scene */
    public GameScene(VertexBufferObjectManager vertexBufferObjectManager) {
        setBackground(new SpriteBackground(new Sprite(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getInstance().getElement(StringConstants.FILE_BACKGROUND),
                vertexBufferObjectManager)));
    }

    /** init scene touch events so user can collaborate with game by screen touches */

    /**
     * Creates and init {@link com.gmail.yaroslavlancelot.eafall.game.touch.GameSceneTouchListener}
     * and assign it to the {@link GameScene} instance.
     * <br/>
     * Set camera coordinates to music and sound handler with using
     * {@link com.gmail.yaroslavlancelot.eafall.game.sound.MusicAndSoundsHandler#setCameraCoordinates(com.gmail.yaroslavlancelot.eafall.game.touch.ICameraCoordinates)}
     *
     * @param windowManager         used to find screen ratio needed for touch lister instance creation
     * @param smoothCamera          camera to pass to the scene touch listener
     * @param musicAndSoundsHandler will have it's initialization here
     */
    public void initGameSceneTouch(WindowManager windowManager, SmoothCamera smoothCamera,
                                   MusicAndSoundsHandler musicAndSoundsHandler) {
        LoggerHelper.methodInvocation(TAG, "initGameSceneTouch");
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float screenToSceneRatio = metrics.widthPixels / SizeConstants.GAME_FIELD_WIDTH;
        /* main scene touch listener */
        GameSceneTouchListener gameSceneTouchListener = new GameSceneTouchListener(smoothCamera, screenToSceneRatio);
        setOnSceneTouchListener(gameSceneTouchListener);

        musicAndSoundsHandler.setCameraCoordinates(gameSceneTouchListener);
    }
}
