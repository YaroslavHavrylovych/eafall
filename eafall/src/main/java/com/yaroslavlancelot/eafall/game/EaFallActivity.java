package com.yaroslavlancelot.eafall.game;

import android.app.Dialog;
import android.os.Build;
import android.widget.Toast;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.dialog.ExitConfirmationDialog;
import com.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.camera.EaFallCamera;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector.SelectorFactory;
import com.yaroslavlancelot.eafall.game.events.aperiodic.endgame.ActivityCloseEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.ShowToastEvent;
import com.yaroslavlancelot.eafall.game.resources.IResourcesLoader;
import com.yaroslavlancelot.eafall.game.resources.ResourceFactory;
import com.yaroslavlancelot.eafall.game.scene.SceneManager;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.general.EbSubscribersHolder;
import com.yaroslavlancelot.eafall.general.SelfCleanable;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
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
public abstract class EaFallActivity extends BaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = EaFallActivity.class.getCanonicalName();
    /** user static area */
    protected BaseGameHud mHud;
    /** game camera */
    protected EaFallCamera mCamera;
    /** background music */
    protected BackgroundMusic mBackgroundMusic;
    /** scene manager */
    protected volatile SceneManager mSceneManager;
    /** resource loader */
    protected IResourcesLoader mResourcesLoader;
    /** exit with double click */
    private long mBackButtonLastClick = 0;
    /** back button single click hint */
    private TimerHandler mExitHintHandler;

    @Override
    public EngineOptions onCreateEngineOptions() {
        //TODO logger was here
        GameState.resetState();
        //pre-in-game
        GameObject.clearCounter();
        // init camera
        mCamera = new EaFallCamera(0, 0,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                EaFallApplication.getConfig().getMaxZoomFactor());
        mCamera.setBounds(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        mCamera.setBoundsEnabled(true);
        //hud
        mHud = createHud();
        mHud.setTouchAreaBindingOnActionDownEnabled(true);
        mHud.setOnAreaTouchTraversalFrontToBack();
        mHud.setAlpha(EaFallApplication.getConfig().getHudAlpha());
        mCamera.setHUD(mHud);
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
                EaFallApplication.getConfig().getMaxSimultaneousSoundStreams());
        audioOptions.setNeedsSound(true);
        //music
        audioOptions.setNeedsMusic(true);
        //game resources
        EbSubscribersHolder.register(this);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        //TODO logger was here

        mResourcesLoader.loadProfilingFonts(getTextureManager(), getFontManager());
        mResourcesLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());
        //sound && music
        SoundFactory.init(getSoundManager());
        mBackgroundMusic = new BackgroundMusic(createMusicPath(), getMusicManager(), this);
        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback onCreateSceneCallback) {
        //TODO logger was here
        mSceneManager.initSplashScene();
        onCreateSceneCallback.onCreateSceneFinished(mSceneManager.getSplashScene());
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        profile();
        onPopulateSceneCallback.onPopulateSceneFinished();

        initExitHint();
        startAsyncResourceLoading();
    }

    @Override
    public void onResumeGame() {
        super.onResumeGame();
        mBackgroundMusic.playBackgroundMusic();
    }

    @Override
    public void onPauseGame() {
        super.onPauseGame();
        mBackgroundMusic.pauseBackgroundMusic();
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        long delta = time - mBackButtonLastClick;

        if (delta < TouchHelper.mMultipleClickTime) {
            mEngine.unregisterUpdateHandler(mExitHintHandler);
            checkedGameClose();
            return;
        } else if (delta > TouchHelper.mMultipleClickDividerTime) {
            mEngine.unregisterUpdateHandler(mExitHintHandler);
            mExitHintHandler.reset();
            mEngine.registerUpdateHandler(mExitHintHandler);
        }
        mBackButtonLastClick = time;
    }

    @Override
    public void finish() {
        super.finish();
        SelfCleanable.clearMemory();
    }

    protected abstract BaseGameHud createHud();

    protected abstract String createMusicPath();

    private void initExitHint() {
        mExitHintHandler = new TimerHandler(TouchHelper.mMultipleClickHintTime,
                new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        mEngine.unregisterUpdateHandler(pTimerHandler);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EaFallActivity.this,
                                        LocaleImpl.getInstance().getStringById(R.string.exit_click),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                });
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final ShowToastEvent showToastEvent) {
        int[] ids = showToastEvent.getTextId();
        String format = LocaleImpl.getInstance().getStringById(ids[0]);
        String result;
        if (ids.length > 1) {
            Object[] args = new Object[ids.length - 1];
            for (int i = 1; i < ids.length; i++) {
                args[i - 1] = LocaleImpl.getInstance().getStringById(ids[i]);
            }
            result = String.format(format, args);
        } else {
            result = format;
        }
        Toast toast =
                Toast.makeText(EaFallActivity.this, result, showToastEvent.isLongShowedToast()
                        ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        if (showToastEvent.isWithoutBackground()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toast.getView().setBackgroundColor(getResources()
                        .getColor(android.R.color.transparent, getTheme()));
            } else {
                toast.getView().setBackgroundColor(getResources()
                        .getColor(android.R.color.transparent));
            }
        }
        toast.show();
    }

    @SuppressWarnings("unused")
    public void onEvent(ActivityCloseEvent activityCloseEvent) {
        checkedGameClose();
    }

    /**
     * Check the current game state before close the game.
     * Can show alert (to confirm game closing)
     * or prevent closing if resource loading in progress (as it can cause crashes).
     */
    protected void checkedGameClose() {
        boolean resourcesLoaded = GameState.isResourcesLoaded();
        //TODO logger was here
        if (GameState.isGameEnded()) {
            finish();
        } else if (!resourcesLoaded) {
            Toast.makeText(this,
                    LocaleImpl.getInstance().getStringById(R.string.exit_loading),
                    Toast.LENGTH_LONG).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Dialog dialog = new ExitConfirmationDialog(EaFallActivity.this);
                    dialog.show();
                }
            });
        }
    }

    /** sets current game state using {@link GameState#setState(GameState.State)} */
    protected boolean setState(GameState.State state) {
        GameState.State currentState = GameState.getState();
        if (state == currentState) {
            return false;
        }
        switch (state) {
            case PAUSED: {
                if (currentState != GameState.State.RESUMED) {
                    return false;
                }
                mSceneManager.getWorkingScene().setIgnoreUpdate(true);
                break;
            }
            case RESUMED: {
                if (currentState == GameState.State.RESOURCE_LOADING) {
                    break;
                } else if (currentState != GameState.State.PAUSED) {
                    return false;
                }
                mSceneManager.getWorkingScene().setIgnoreUpdate(false);
                break;
            }
        }
        GameState.setState(state);
        return true;
    }

    /** triggers {@link #startAsyncResourceLoading(Runnable)} with null argument */
    protected void startAsyncResourceLoading() {
        startAsyncResourceLoading(null);
    }

    /**
     * invokes {@link #loadResources()} in other thread.
     *
     * @param runnable if not null then {@link Runnable#run()} method will be excecuted as
     *                 the first method of the newly created {@link Thread}
     */
    protected void startAsyncResourceLoading(final Runnable runnable) {
        GameState.resourceLoading();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (runnable != null) {
                    runnable.run();
                }
                preResourcesLoading();
                loadResources();
                onResourcesLoaded();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    protected abstract void preResourcesLoading();

    /** show profiling information on screen (using FPS logger) */
    protected void profile() {
        final Text fpsText = new Text(
                SizeConstants.GAME_FIELD_WIDTH / 2, SizeConstants.GAME_FIELD_HEIGHT - 150,
                FontHolder.getInstance().getElement("profiling"),
                "fps: 60.00", 20, getVertexBufferObjectManager());
        fpsText.setColor(Color.GREEN);
        mHud.attachChild(fpsText);
        final FPSLogger fpsLogger = new FPSLogger(1) {
            @Override
            protected void onLogFPS() {
                fpsText.setText(String.format("fps: %.2f", this.mFrames / this.mSecondsElapsed));
            }
        };
        ApplicationSettings settings = EaFallApplication.getConfig().getSettings();
        settings.setOnConfigChangedListener(settings.KEY_PREF_DEVELOPERS_MODE,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        boolean bValue = (Boolean) value;
                        fpsText.setVisible(bValue);
                        if (bValue) {
                            mEngine.registerUpdateHandler(fpsLogger);
                        } else {
                            mEngine.unregisterUpdateHandler(fpsLogger);
                        }
                    }
                }
        );
        boolean enabled = settings.isProfilingEnabled();
        fpsText.setVisible(enabled);
        if (enabled) {
            mEngine.registerUpdateHandler(fpsLogger);
        }
    }

    /** triggers when all initialized, splash showed and you can load resources */
    protected abstract void loadResources();

    /** have to be triggered after resources will be loaded */
    public void onResourcesLoaded() {
        initWorkingScene();
    }

    protected void initWorkingScene() {
        mSceneManager.initWorkingScene(mCamera);
        onPopulateWorkingScene(mSceneManager.getWorkingScene());
        SelectorFactory.init(getVertexBufferObjectManager(), mSceneManager.getWorkingScene());
    }

    protected abstract void onPopulateWorkingScene(EaFallScene scene);

    /** Hide splash scene and shows working scene */
    protected void hideSplash() {
        mSceneManager.hideSplash();
        mSceneManager.clearSplashScene();
        mResourcesLoader.unloadSplashImages();
        if (mBackgroundMusic != null) {
            mBackgroundMusic.playBackgroundMusic();
        }
        onShowWorkingScene();
        GameState.setState(GameState.State.RESUMED);
    }

    /** triggers in the main engine thread after the splash had already being hidden */
    protected abstract void onShowWorkingScene();
}
