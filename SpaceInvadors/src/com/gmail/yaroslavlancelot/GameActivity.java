package com.gmail.yaroslavlancelot;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Main game Activity.
 */
public class GameActivity extends SimpleBaseGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    // ===========================================================
    // Fields
    // ===========================================================

    private Camera mCamera;
    private Scene mMainScene;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
    }

    @Override
    protected void onCreateResources() {
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        this.mMainScene = new Scene();

        return this.mMainScene;
    }

}