package com.gmail.yaroslavlancelot.spaceinvaders.scenes.manager;

import android.content.Intent;

import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.MainOperationsBaseGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupManager;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.scenes.GameBackgroundScene;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
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

    private Scene mSplashScene, mGameScene;

    private MainOperationsBaseGameActivity mActivity;
    private Engine mEngine;
    private SmoothCamera mCamera;
    private TextureRegionHolderUtils mTextureRegionHolderUtils;
    /** user static area */
    protected HUD mHud;

    /** background theme */
    private MusicAndSoundsHandler mMusicAndSoundsHandler;
    private MusicAndSoundsHandler.BackgroundMusic mBackgroundMusic;

    public SceneManager(MainOperationsBaseGameActivity activity, Engine engine, SmoothCamera camera){
        mActivity = activity;
        mEngine = engine;
        mCamera = camera;
        mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();
    }

    public void loadSplashResources() {
        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(this.mActivity.getTextureManager(), 128, 32, TextureOptions.DEFAULT);
        mTextureRegionHolderUtils.addElement(StringsAndPathUtils.KEY_SPLASH_SCREEN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        splashTextureAtlas, this.mActivity, StringsAndPathUtils.FILE_SPLASH_SCREEN, 0, 0)
        );
        splashTextureAtlas.load();
    }

    public Scene createSplashScene() {
        mSplashScene = new Scene();
        Sprite splash = new Sprite(0, 0, mTextureRegionHolderUtils.getElement(StringsAndPathUtils.KEY_SPLASH_SCREEN),
                mEngine.getVertexBufferObjectManager());

        splash.setScale(4f);
        splash.setPosition((SizeConstants.GAME_FIELD_WIDTH - splash.getWidth()) * 0.5f,
                (SizeConstants.GAME_FIELD_HEIGHT - splash.getHeight()) * 0.5f);
        mSplashScene.attachChild(splash);
        return mSplashScene;
    }

    public void loadInGameResources() {
        LoggerHelper.methodInvocation(TAG, "onCreateGameResources");
        // music
        mMusicAndSoundsHandler = new MusicAndSoundsHandler(mActivity.getSoundManager(), mActivity);
        mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(mActivity.getMusicManager());

        //races loadGeneralGameTextures
        Intent intent = mActivity.getIntent();
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPathUtils.FIRST_TEAM_ALLIANCE),
                mActivity.getVertexBufferObjectManager(), mMusicAndSoundsHandler);
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPathUtils.SECOND_TEAM_ALLIANCE),
                mActivity.getVertexBufferObjectManager(), mMusicAndSoundsHandler);
        for (IAlliance race : AllianceHolder.getInstance().getElements()) {
            race.loadResources(mActivity.getTextureManager());
        }

        // other loader
        PopupManager.loadResource(mActivity, mActivity.getTextureManager());
        TextureRegionHolderUtils.loadGeneralGameTextures(mActivity, mActivity.getTextureManager());
        GameBackgroundScene.loadImages(mActivity.getTextureManager());

        // font
        FontHolderUtils.loadGeneralGameFonts(mActivity.getFontManager(), mActivity.getTextureManager());
        DescriptionPopupHud.loadFonts(mActivity.getFontManager(), mActivity.getTextureManager());
    }

    public Scene createInGameScene() {
        //game scene
        GameBackgroundScene gameBackgroundScene = new GameBackgroundScene(mActivity.getVertexBufferObjectManager());
        gameBackgroundScene.initGameSceneTouch(mActivity.getWindowManager(), mCamera, mMusicAndSoundsHandler);
        mGameScene = gameBackgroundScene;
        return mGameScene;
    }

    public void replaceSplashSceneWithGameScene() {
        mSplashScene.detachSelf();
        mEngine.setScene(mGameScene);
    }


}
