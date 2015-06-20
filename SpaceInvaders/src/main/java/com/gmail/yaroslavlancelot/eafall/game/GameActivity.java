package com.gmail.yaroslavlancelot.eafall.game;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.resources.IResourcesLoader;
import com.gmail.yaroslavlancelot.eafall.game.resources.ResourceFactory;
import com.gmail.yaroslavlancelot.eafall.game.scene.SceneManager;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.AudioOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

/**
 * Base activity for all activities which has to use AndEngine.
 * <br/>
 * Base:
 * <ul>
 * <li>Shows splash screen while resources loading.</li>
 * <li>Contains Scene and Resource managers.</li>
 * <li>Track when to hide the splash screen (checking fps).</li>
 * <li>Developer mode signs (fps) applies here.</li>
 * </ul>
 *
 * @author Yaroslav Havrylovych
 */
public abstract class GameActivity extends BaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = GameActivity.class.getCanonicalName();
    /** user static area */
    protected HUD mHud;
    /** game camera */
    protected EaFallCamera mCamera;
    /** background music */
    protected BackgroundMusic mBackgroundMusic;
    /** scene manager */
    protected volatile SceneManager mSceneManager;
    /** resource loader */
    protected IResourcesLoader mResourcesLoader;

    @Override
    public EngineOptions onCreateEngineOptions() {
        LoggerHelper.methodInvocation(TAG, "onCreateEngineOptions");
        //pre-in-game
        GameObject.clearCounter();
        // init camera
        mCamera = new EaFallCamera(0, 0,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                Config.getConfig().getMaxZoomFactor());
        mCamera.setBounds(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        mCamera.setBoundsEnabled(true);
        mCamera.setHUD(new HUD());
        //hud
        mHud = mCamera.getHUD();
        mHud.setTouchAreaBindingOnActionDownEnabled(true);
        mHud.setOnAreaTouchTraversalFrontToBack();
        //resource manager
        ResourceFactory.TypeResourceLoader typeResourceLoader = (ResourceFactory.TypeResourceLoader)
                getIntent().getExtras().getSerializable(ResourceFactory.RESOURCE_LOADER);
        mResourcesLoader = new ResourceFactory().getLoader(typeResourceLoader == null
                ? ResourceFactory.TypeResourceLoader.CLIENT
                : typeResourceLoader);
        //scene manager
        mSceneManager = new SceneManager(this);
        //other
        EngineOptions engineOptions = new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT), mCamera
        );
        //audio
        AudioOptions audioOptions = engineOptions.getAudioOptions();
        //sound
        audioOptions.getSoundOptions().setMaxSimultaneousStreams(
                Config.getConfig().getMaxSimultaneousSoundStreams());
        audioOptions.setNeedsSound(Config.getConfig().isSoundsEnabled());
        //music
        audioOptions.setNeedsMusic(Config.getConfig().isMusicEnabled());
        return engineOptions;
    }


    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateResources");

        if (Config.getConfig().isProfilingEnabled()) {
            mResourcesLoader.loadProfilingFonts(getTextureManager(), getFontManager());
        }
        mResourcesLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());
        //init sounds
        SoundFactory.init(getSoundManager(), GameActivity.this);

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback onCreateSceneCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateScene");
        mSceneManager.initSplashScene();
        onCreateSceneCallback.onCreateSceneFinished(mSceneManager.getSplashScene());
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        if (Config.getConfig().isProfilingEnabled()) {
            profile();
        }
        onPopulateSceneCallback.onPopulateSceneFinished();

        mSceneManager.initWorkingScene(mCamera);
        new Thread(new Runnable() {
            @Override
            public void run() {
                asyncResourcesLoading();
            }
        }).start();
    }

    @Override
    public synchronized void onResumeGame() {
        super.onResumeGame();
        if (mBackgroundMusic != null)
            mBackgroundMusic.playBackgroundMusic();
    }

    @Override
    public synchronized void onPauseGame() {
        super.onPauseGame();
        if (mBackgroundMusic != null)
            mBackgroundMusic.pauseBackgroundMusic();
    }

    /** show profiling information on screen (using FPS logger) */
    protected void profile() {
        final Text fpsText = new Text(200, SizeConstants.GAME_FIELD_HEIGHT - 50,
                FontHolder.getInstance().getElement("profiling"),
                "fps: 60.00", 20, getVertexBufferObjectManager());
        fpsText.setColor(Color.GREEN);
        mHud.attachChild(fpsText);
        mEngine.registerUpdateHandler(new FPSLogger(1) {
            @Override
            protected void onLogFPS() {
                fpsText.setText(String.format("fps: %.2f", this.mFrames / this.mSecondsElapsed));
            }
        });
    }

    /** triggers when all initialized, splash showed and you can load resources */
    protected abstract void asyncResourcesLoading();

    /** have to be triggered after resources will be loaded */
    public abstract void onResourcesLoaded();

    /** Hide splash scene and shows working scene */
    public void hideSplash() {
        mSceneManager.hideSplash();
    }
}
