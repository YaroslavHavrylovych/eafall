package com.gmail.yaroslavlancelot;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Main game Activity.
 */
public class GameActivity extends BaseGameActivity {
    private static final int sCameraWidth = 1600;
    private static final int sCameraHeight = 1000;
    private ITextureRegion mPlayerTextureRegion;

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, sCameraWidth, sCameraHeight);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(sCameraWidth, sCameraHeight), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        BitmapTextureAtlas playerTexture = new BitmapTextureAtlas(getTextureManager(), 64, 64);
        mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playerTexture, this, "icon.png", 0, 0);
        playerTexture.load();
        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {
        Scene scene = new Scene();
        scene.setBackground(new Background(0, 125, 58));
        onCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        Sprite player = new Sprite(sCameraWidth / 2, sCameraHeight / 2, mPlayerTextureRegion, mEngine.getVertexBufferObjectManager());
        player.setRotation(45.f);
        scene.attachChild(player);
        onPopulateSceneCallback.onPopulateSceneFinished();
    }
}