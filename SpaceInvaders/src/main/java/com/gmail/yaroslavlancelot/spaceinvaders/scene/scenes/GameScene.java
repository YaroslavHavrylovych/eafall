package com.gmail.yaroslavlancelot.spaceinvaders.scene.scenes;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
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
    private static String mBackgroundImagePath = StringsAndPathUtils.getPathToGeneralImages() + "background.png";

    /** set background image to the scene */
    public GameScene(VertexBufferObjectManager vertexBufferObjectManager) {
        setBackground(new SpriteBackground(new Sprite(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(mBackgroundImagePath),
                vertexBufferObjectManager)));
    }

    /** loads game scene background */
    public static void loadImages(TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(mBackgroundImagePath,
                smallObjectTexture, SpaceInvadersApplication.getContext(), 0, 0);
        smallObjectTexture.load();
    }

    /** init scene touch events so user can collaborate with game by screen touches */

    /**
     * Creates and init {@link com.gmail.yaroslavlancelot.spaceinvaders.objects.touch.MainSceneTouchListener}
     * and assign it to the {@link GameScene} instance.
     * <br/>
     * Set camera coordinates to music and sound handler with using
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.MusicAndSoundsHandler#setCameraCoordinates(com.gmail.yaroslavlancelot.spaceinvaders.objects.ICameraCoordinates)}
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
        MainSceneTouchListener mainSceneTouchListener = new MainSceneTouchListener(smoothCamera, screenToSceneRatio);
        setOnSceneTouchListener(mainSceneTouchListener);

        musicAndSoundsHandler.setCameraCoordinates(mainSceneTouchListener);
    }
}
