package com.gmail.yaroslavlancelot.spaceinvaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.SimpleWarrior;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.constants.IGameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.touch.ISpriteTouchListener;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main game Activity.
 */
public class GameActivity extends BaseGameActivity {
    public static final String TAG = GameActivity.class.getCanonicalName();
    private static final int sCameraWidth = 800;
    private static final int sCameraHeight = 480;
    private final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
    private ITextureRegion mSunTextureRegion;
    private ITextureRegion mRedPlanetTextureRegion;
    private ITextureRegion mBluePlanetTextureRegion;
    private ITextureRegion mRedWarriorTextureRegion;
    private ITextureRegion mBlueWarriorTextureRegion;
    private PhysicsWorld mPhysicsWorld;
    private Scene mScene;
    private HashMap<String, StaticObject> mStaticObjects = new HashMap<String, StaticObject>();
    private ArrayList<SimpleWarrior> mSimpleWarriorObjects = new ArrayList<SimpleWarrior>();

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, sCameraWidth, sCameraHeight);
        return new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(sCameraWidth, sCameraHeight), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        BitmapTextureAtlas simpleWarriorTexture = new BitmapTextureAtlas(getTextureManager(), 24, 12);
        mRedWarriorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(simpleWarriorTexture, this, IGameObjectsConstants.FILE_RED_WARRIOR, 0, 0);
        mBlueWarriorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(simpleWarriorTexture, this, IGameObjectsConstants.FILE_BLUE_WARRIOR, 12, 0);
        simpleWarriorTexture.load();

        BitmapTextureAtlas staticObjectsTexture = new BitmapTextureAtlas(getTextureManager(), 192, 64);
        mSunTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(staticObjectsTexture, this, IGameObjectsConstants.FILE_SUN, 0, 0);
        mRedPlanetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(staticObjectsTexture, this, IGameObjectsConstants.FILE_RED_PLANET, 64, 0);
        mBluePlanetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(staticObjectsTexture, this, IGameObjectsConstants.FILE_BLUE_PLANET, 128, 0);
        staticObjectsTexture.load();

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) {
        mScene = new Scene();
        mScene.setBackground(new Background(0, 0, 0));
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        mScene.registerUpdateHandler(mPhysicsWorld);
        // create Sun
        createStaticObject(sCameraWidth / 2 - 32, sCameraHeight / 2 - 32, mSunTextureRegion, IGameObjectsConstants.KEY_SUN);
        // create red planet
        createStaticObject(0, sCameraHeight / 2 - 32, mRedPlanetTextureRegion, IGameObjectsConstants.KEY_RED_PLANET);
        // create blue planet
        createStaticObject(sCameraWidth - 64, sCameraHeight / 2 - 32, mBluePlanetTextureRegion, IGameObjectsConstants.KEY_BLUE_PLANET);

        initPlanetTouchListeners();

        onCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    private Sprite createStaticObject(float x, float y, ITextureRegion textureRegion, String key) {
        LoggerHelper.methodInvocation(TAG, "createStaticObject");
        StaticObject staticObjectSprite = new StaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(mPhysicsWorld, staticObjectSprite, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        mScene.attachChild(staticObjectSprite);
        mStaticObjects.put(key, staticObjectSprite);
        return staticObjectSprite;
    }

    private SimpleWarrior createDynamicObjects(float x, float y, ITextureRegion textureRegion, Scene scene) {
        LoggerHelper.methodInvocation(TAG, "createDynamicObjects");
        SimpleWarrior simpleWarrior = new SimpleWarrior(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, simpleWarrior, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        simpleWarrior.setBody(body);
        scene.attachChild(simpleWarrior);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(simpleWarrior, body, true, true));
        mSimpleWarriorObjects.add(simpleWarrior);
        return simpleWarrior;
    }

    public void initPlanetTouchListeners() {
        ISpriteTouchListener redPlanetTouchListener = new ISpriteTouchListener() {
            @Override
            public boolean onTouch(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                LoggerHelper.methodInvocation(TAG, "initPlanetTouchListeners.planetOnTouchListener");
                if (pSceneTouchEvent.isActionUp())
                    createDynamicObjects(pSceneTouchEvent.getX() + 5, pSceneTouchEvent.getY(), mRedWarriorTextureRegion, mScene);
                return true;
            }
        };
        StaticObject staticObject = mStaticObjects.get(IGameObjectsConstants.KEY_RED_PLANET);
        staticObject.setOnTouchListener(redPlanetTouchListener);
        mScene.registerTouchArea(staticObject);

        ISpriteTouchListener bluePlanetTouchListener = new ISpriteTouchListener() {
            @Override
            public boolean onTouch(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                LoggerHelper.methodInvocation(TAG, "initPlanetTouchListeners.planetOnTouchListener");
                if (pSceneTouchEvent.isActionUp())
                    createDynamicObjects(pSceneTouchEvent.getX() - 15, pSceneTouchEvent.getY(), mBlueWarriorTextureRegion, mScene);
                return true;
            }
        };
        staticObject = mStaticObjects.get(IGameObjectsConstants.KEY_BLUE_PLANET);
        staticObject.setOnTouchListener(bluePlanetTouchListener);
        mScene.registerTouchArea(staticObject);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }
}