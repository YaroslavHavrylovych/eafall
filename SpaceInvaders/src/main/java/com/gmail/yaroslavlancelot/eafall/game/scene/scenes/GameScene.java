package com.gmail.yaroslavlancelot.eafall.game.scene.scenes;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.SunStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.touch.GameSceneTouchListener;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.sound.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

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
    private static String mBackgroundImagePath = StringsAndPath.getPathToGeneralImages() + "background.png";

    /** set background image to the scene */
    public GameScene(VertexBufferObjectManager vertexBufferObjectManager) {
        setBackground(new SpriteBackground(new Sprite(0, 0,
                TextureRegionHolder.getInstance().getElement(mBackgroundImagePath),
                vertexBufferObjectManager)));
    }

    /** loads game scene background */
    public static void loadImages(TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                Sizes.GAME_FIELD_WIDTH, Sizes.GAME_FIELD_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(mBackgroundImagePath,
                smallObjectTexture, EaFallApplication.getContext(), 0, 0);
        smallObjectTexture.load();
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        LoggerHelper.methodInvocation(TAG, "loadResources");
        SunStaticObject.loadImages(context, textureManager);
        PlanetStaticObject.loadImages(context, textureManager);
        GameScene.loadImages(textureManager);
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
        float screenToSceneRatio = metrics.widthPixels / Sizes.GAME_FIELD_WIDTH;
        /* main scene touch listener */
        GameSceneTouchListener gameSceneTouchListener = new GameSceneTouchListener(smoothCamera, screenToSceneRatio);
        setOnSceneTouchListener(gameSceneTouchListener);

        musicAndSoundsHandler.setCameraCoordinates(gameSceneTouchListener);
    }
}
