package com.gmail.yaroslavlancelot.spaceinvaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.SimpleUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.IGameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
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
    private static final int sCameraWidth = 800;
    /** Camera height. Height of part of the screen which is currently viewed by user */
    private static final int sCameraHeight = 480;
    /** {@link FixtureDef} for obstacles (static bodies) */
    private final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
    /*
     * ITexture definitions
     */
    private ITextureRegion mSunTextureRegion;
    private ITextureRegion mRedPlanetTextureRegion;
    private ITextureRegion mBluePlanetTextureRegion;
    private ITextureRegion mRedWarriorTextureRegion;
    @SuppressWarnings("unused")
    private ITextureRegion mBlueWarriorTextureRegion;
    /** current game physics world */
    private PhysicsWorld mPhysicsWorld;
    /** currently viewed by user screen */
    private Scene mScene;
    /** contains game obstacles and other static objects */
    private HashMap<String, StaticObject> mStaticObjects = new HashMap<String, StaticObject>();
    /** contains whole game units/warriors */
    private ArrayList<SimpleUnit> mSimpleUnitObjects = new ArrayList<SimpleUnit>();
    /** all teams in current game */
    private List<ITeam> mITeamsList = new ArrayList<ITeam>();
    /** red team */
    private Team mRedTeam;
    /** blue team */
    private Team mBlueTeam;

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, sCameraWidth, sCameraHeight);
        return new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(sCameraWidth, sCameraHeight), camera);
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
        mITeamsList.add(mRedTeam);
        // create blue planet
        mBlueTeam = new Team(IGameObjectsConstants.BLUE_TEAM_NAME, createPlanet(sCameraWidth - 64, sCameraHeight / 2 - 32, mBluePlanetTextureRegion, IGameObjectsConstants.KEY_BLUE_PLANET));
        mBlueTeam.getTeamPlanet().setSpawnPoint(sCameraWidth - 32 - 15, sCameraHeight / 2);
        mITeamsList.add(mBlueTeam);

        initPlanetTouchListeners();

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
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link SimpleUnit}
     *
     * @return newly created {@link SimpleUnit}
     */
    private SimpleUnit createSimpleUnit(float x, float y, ITextureRegion textureRegion, Scene scene) {
        LoggerHelper.methodInvocation(TAG, "createSimpleUnit");
        SimpleUnit mSimpleUnit = new SimpleUnit(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        scene.attachChild(mSimpleUnit);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, mSimpleUnit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        mSimpleUnit.setBody(body);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mSimpleUnit, body, true, true));
        mSimpleUnitObjects.add(mSimpleUnit);
        return mSimpleUnit;
    }

    private void initPlanetTouchListeners() {
        initPlanetTouchListener(mRedTeam);
        initPlanetTouchListener(mBlueTeam);
    }

    private void initPlanetTouchListener(final ITeam initiatedTeam) {
        for (final ITeam team : mITeamsList) {
            if (!initiatedTeam.isFriendlyTeam(team)) {
                final StaticObject initiatedTeamPlanet = initiatedTeam.getTeamPlanet();
                ISpriteTouchListener initiatedTeamPlanetTouchListener = new ISpriteTouchListener() {
                    @Override
                    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                        LoggerHelper.methodInvocation(TAG, "initPlanetTouchListeners.planetOnTouchListener");
                        if (!pSceneTouchEvent.isActionUp()) return true;

                        ITextureRegion textureRegion = initiatedTeam.getTeamName().equals(IGameObjectsConstants.RED_TEAM_NAME) ? mRedWarriorTextureRegion : mBlueWarriorTextureRegion;
                        createUnitForTeam(textureRegion, initiatedTeam, team);
                        return true;
                    }
                };
                initiatedTeamPlanet.setOnTouchListener(initiatedTeamPlanetTouchListener);
                mScene.registerTouchArea(initiatedTeamPlanet);
                break;
            }
        }
    }

    private SimpleUnit createUnitForTeam(final ITextureRegion textureRegion, final ITeam unitTeam, final ITeam teamAgainst) {
        SimpleUnit warrior = createSimpleUnit(unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY(), textureRegion, mScene);
        unitTeam.addObjectToTeam(warrior);
        warrior.setMainTarget(teamAgainst.getTeamPlanet().getSpawnPointX(), teamAgainst.getTeamPlanet().getSpawnPointY());
        return warrior;
    }
}