package com.gmail.yaroslavlancelot.spaceinvaders;

import android.util.DisplayMetrics;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.IGameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.gameevents.ISimpleUnitDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.gameevents.ISimpleUnitEnemiesUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public class GameActivity extends BaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = GameActivity.class.getCanonicalName();
    /** Camera width. Width of part of the screen which is currently viewed by user */
    public static final int sCameraWidth = 800;
    /** Camera height. Height of part of the screen which is currently viewed by user */
    public static final int sCameraHeight = 480;
    /** {@link FixtureDef} for obstacles (static bodies) */
    private final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
    /*
     * ITexture definitions
     */
    private ITextureRegion mSunTextureRegion;
    private ITextureRegion mRedPlanetTextureRegion;
    private ITextureRegion mBluePlanetTextureRegion;
    private ITextureRegion mRedWarriorTextureRegion;
    private ITextureRegion mBlueWarriorTextureRegion;
    /** current game physics world */
    private PhysicsWorld mPhysicsWorld;
    /** currently viewed by user screen */
    private Scene mScene;
    /** contains game obstacles and other static objects */
    private HashMap<String, StaticObject> mStaticObjects = new HashMap<String, StaticObject>();
    /** contains whole game units/warriors */
    private ArrayList<Unit> mUnits = new ArrayList<Unit>(50);
    /** all teams in current game */
    private List<ITeam> mTeams = new ArrayList<ITeam>();
    /** red team */
    private Team mRedTeam;
    /** blue team */
    private Team mBlueTeam;
    /** game camera */
    private SmoothCamera mCamera;
    /** */
    private ISimpleUnitEnemiesUpdater mRedTeamEnemiesUpdater = new RedPlanetSimpleUnitEnemiesUpdater();
    /** */
    private ISimpleUnitEnemiesUpdater mBlueTeamEnemiesUpdater = new BluePlanetSimpleUnitEnemiesUpdater();
    /** */
    private ISimpleUnitDestroyedListener mRedTeamDestroyedListener = new RedPlanetSimpleUnitDestroyedListener();
    /** */
    private ISimpleUnitDestroyedListener mBlueTeamDestroyedListener = new BluePlanetSimpleUnitDestroyedListener();

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new SmoothCamera(0, 0, sCameraWidth, sCameraHeight, sCameraWidth, sCameraHeight, 1.0f);
        // multi-touch
        if(!MultiTouch.isSupported(this)) {
            LoggerHelper.printErrorMessage(TAG, "MultiTouch isn't supported");
            finish();
        }

        return new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(sCameraWidth, sCameraHeight), mCamera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        BitmapTextureAtlas simpleWarriorTexture = new BitmapTextureAtlas(getTextureManager(), 24, 12, TextureOptions.BILINEAR);
        mRedWarriorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(simpleWarriorTexture, this, IGameObjectsConstants.FILE_RED_WARRIOR, 0, 0);
        mBlueWarriorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(simpleWarriorTexture, this, IGameObjectsConstants.FILE_BLUE_WARRIOR, 12, 0);
        simpleWarriorTexture.load();

        BitmapTextureAtlas staticObjectsTexture = new BitmapTextureAtlas(getTextureManager(), 192, 64, TextureOptions.BILINEAR);
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
        mRedTeam = new Team(IGameObjectsConstants.RED_TEAM_NAME, createPlanet(0, sCameraHeight / 2 - 32, mRedPlanetTextureRegion, IGameObjectsConstants.KEY_RED_PLANET));
        mRedTeam.getTeamPlanet().setSpawnPoint(32 + 15, sCameraHeight / 2);
        mTeams.add(mRedTeam);
        // create blue planet
        mBlueTeam = new Team(IGameObjectsConstants.BLUE_TEAM_NAME, createPlanet(sCameraWidth - 64, sCameraHeight / 2 - 32, mBluePlanetTextureRegion, IGameObjectsConstants.KEY_BLUE_PLANET));
        mBlueTeam.getTeamPlanet().setSpawnPoint(sCameraWidth - 32 - 15, sCameraHeight / 2);
        mTeams.add(mBlueTeam);

        initPlanetsTouchListeners();

        initSceneTouch();

        onCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    /**
     * create static game object
     *
     * @param x abscissa (top left corner) of created static object
     * @param y ordinate (top left corner) of created static object
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link StaticObject}
     * @param key key of current static object
     *
     * @return newly created {@link StaticObject}
     */
    private StaticObject createStaticObject(float x, float y, ITextureRegion textureRegion, String key) {
        LoggerHelper.methodInvocation(TAG, "createStaticObject");
        StaticObject staticObjectSprite = new StaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(mPhysicsWorld, staticObjectSprite, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        mScene.attachChild(staticObjectSprite);
        mStaticObjects.put(key, staticObjectSprite);
        return staticObjectSprite;
    }

    /**
     * create planet game object
     *
     * @param x abscissa (top left corner) of created planet
     * @param y ordinate (top left corner) of created planet
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link PlanetStaticObject}
     * @param key key of current planet
     *
     * @return newly created {@link PlanetStaticObject}
     */
    private PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(mPhysicsWorld, planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        mScene.attachChild(planetStaticObject);
        mStaticObjects.put(key, planetStaticObject);
        return planetStaticObject;
    }

    /**
     * create dynamic game object (e.g. warrior or some other stuff)
     *
     * @param x abscissa (top left corner) of created dynamic object
     * @param y ordinate (top left corner) of created dynamic object
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.Unit}
     *
     * @return newly created {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.Unit}
     */
    private Unit createSimpleUnit(float x, float y, ITextureRegion textureRegion, Scene scene) {
        LoggerHelper.methodInvocation(TAG, "createSimpleUnit");
        Unit mUnit = new Unit(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        scene.attachChild(mUnit);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, mUnit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        mUnit.setBody(body);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mUnit, body, true, true));
        mUnits.add(mUnit);
        return mUnit;
    }

    /** init planet touch listeners for all teams */
    private void initPlanetsTouchListeners() {
        initPlanetTouchListener(mRedTeam);
        initPlanetTouchListener(mBlueTeam);
    }

    /** init planet touch listener for some team */
    private void initPlanetTouchListener(final ITeam initiatedTeam) {
        for (final ITeam team : mTeams) {
            if (!initiatedTeam.isFriendlyTeam(team)) {
                final StaticObject initiatedTeamPlanet = initiatedTeam.getTeamPlanet();
                ISpriteTouchListener initiatedTeamPlanetTouchListener = new ISpriteTouchListener() {
                    @Override
                    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                        LoggerHelper.methodInvocation(TAG, "initPlanetsTouchListeners.planetOnTouchListener");
                        if (!pSceneTouchEvent.isActionUp()) return true;

                        ITextureRegion textureRegion = initiatedTeam.getTeamName().equals(IGameObjectsConstants.RED_TEAM_NAME) ? mRedWarriorTextureRegion : mBlueWarriorTextureRegion;
                        createUnitForTeam(textureRegion, initiatedTeam);
                        return true;
                    }
                };
                initiatedTeamPlanet.setOnTouchListener(initiatedTeamPlanetTouchListener);
                mScene.registerTouchArea(initiatedTeamPlanet);
                break;
            }
        }
    }

    private Unit createUnitForTeam(final ITextureRegion textureRegion, final ITeam unitTeam) {
        Unit warrior = createSimpleUnit(unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY(), textureRegion, mScene);
        unitTeam.addObjectToTeam(warrior);
        warrior.setEnemiesUpdater(unitTeam.equals(mRedTeam) ? mRedTeamEnemiesUpdater : mBlueTeamEnemiesUpdater);
        warrior.setUnitDestroyedListener(
                unitTeam.equals(mRedTeam) ? mRedTeamDestroyedListener : mBlueTeamDestroyedListener);
        return warrior;
    }

    private void initSceneTouch() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float screenToSceneRatio = metrics.widthPixels / sCameraWidth;
        mScene.setOnSceneTouchListener(new MainSceneTouchListener(mCamera, this, screenToSceneRatio));
    }

    private void detachUnit(final Unit unit) {
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mScene.detachChild(unit);
            }
        });
    }

    private class RedPlanetSimpleUnitEnemiesUpdater implements ISimpleUnitEnemiesUpdater {
        @Override
        public List<Unit> getEnemies(final Unit unit) {
            return TeamUtils.getEnemiesForUnit(unit, mBlueTeam);
        }
    }

    private class BluePlanetSimpleUnitEnemiesUpdater implements ISimpleUnitEnemiesUpdater {
        @Override
        public List<Unit> getEnemies(final Unit unit) {
            return TeamUtils.getEnemiesForUnit(unit, mRedTeam);
        }
    }

    private class RedPlanetSimpleUnitDestroyedListener implements ISimpleUnitDestroyedListener {
        @Override
        public void unitDestroyed(final Unit unit) {
            mRedTeam.removeObjectFromTeam(unit);
            detachUnit(unit);
        }
    }

    private class BluePlanetSimpleUnitDestroyedListener implements ISimpleUnitDestroyedListener {
        @Override
        public void unitDestroyed(final Unit unit) {
            mBlueTeam.removeObjectFromTeam(unit);
            detachUnit(unit);
        }
    }
}