package com.gmail.yaroslavlancelot.spaceinvaders.scene;

import android.content.Intent;

import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.MainOperationsBaseGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupManager;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.scene.scenes.GameScene;
import com.gmail.yaroslavlancelot.spaceinvaders.scene.scenes.SplashScene;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

/**
 * Created by Merle on 26-01-2015.
 */
public class SceneManager {

    /** tag, which is used for debugging purpose */
    public static final String TAG = SceneManager.class.getCanonicalName();

    private SplashScene mSplashScene;
    private GameScene mGameScene;

    private MainOperationsBaseGameActivity mGameActivity;
    private Engine mEngine;

    public SceneManager(MainOperationsBaseGameActivity gameActivity, Engine engine){
        mGameActivity = gameActivity;
        mEngine = engine;
    }

    public void loadSplashResources() {
        BitmapTextureAtlas splashTextureAtlas =
                new BitmapTextureAtlas(mGameActivity.getTextureManager(), 128, 32, TextureOptions.DEFAULT);
        TextureRegionHolderUtils.getInstance().addElement(
                StringsAndPathUtils.KEY_SPLASH_SCREEN, BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        splashTextureAtlas, mGameActivity, StringsAndPathUtils.FILE_SPLASH_SCREEN, 0, 0)
        );
        splashTextureAtlas.load();
    }

    public SplashScene createSplashScene() {
        mSplashScene = new SplashScene();
        Sprite splash = new Sprite(0, 0, TextureRegionHolderUtils.getInstance()
                .getElement(StringsAndPathUtils.KEY_SPLASH_SCREEN), mEngine.getVertexBufferObjectManager());
        splash.setScale(4f);
        splash.setPosition((SizeConstants.GAME_FIELD_WIDTH - splash.getWidth()) * 0.5f,
                (SizeConstants.GAME_FIELD_HEIGHT - splash.getHeight()) * 0.5f);
        mSplashScene.attachChild(splash);
        return mSplashScene;
    }

    public SplashScene getSplashScene() {
        return mSplashScene;
    }

    public void loadGameResources() {
        LoggerHelper.methodInvocation(TAG, "onCreateGameResources");

        //races loadGeneralGameTextures
        for (IAlliance race : AllianceHolder.getInstance().getElements()) {
            race.loadResources(mGameActivity.getTextureManager());
        }

        // other loader
        PopupManager.loadResource(mGameActivity, mGameActivity.getTextureManager());
        SunStaticObject.loadImages(mGameActivity, mGameActivity.getTextureManager());
        PlanetStaticObject.loadImages(mGameActivity, mGameActivity.getTextureManager());
        GameScene.loadImages(mGameActivity.getTextureManager());

        // font
        FontHolderUtils.loadGeneralGameFonts(mGameActivity.getFontManager(), mGameActivity.getTextureManager());
        DescriptionPopupHud.loadFonts(mGameActivity.getFontManager(), mGameActivity.getTextureManager());
    }

    public GameScene createGameScene(SmoothCamera smoothCamera) {
        //game scene
        GameScene gameScene = new GameScene(mGameActivity.getVertexBufferObjectManager());
        gameScene.initGameSceneTouch(
                mGameActivity.getWindowManager(), smoothCamera, mGameActivity.getmMusicAndSoundsHandler());
        mGameScene = gameScene;
        return mGameScene;
    }

    public void replaceSplashSceneWithGame() {
        mSplashScene.detachSelf();
        mEngine.setScene(mGameScene);
    }

    public GameScene getGameScene() {
        return mGameScene;
    }
}
