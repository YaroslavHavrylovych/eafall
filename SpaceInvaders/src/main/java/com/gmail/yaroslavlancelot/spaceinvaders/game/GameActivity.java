package com.gmail.yaroslavlancelot.spaceinvaders.game;

import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.MoneyUpdateCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.HandsAttacker;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.UserPlanetTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitCallbacksUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public class GameActivity extends BaseGameActivity implements Localizable, EntityOperations {
    /** tag, which is used for debugging purpose */
    public static final String TAG = GameActivity.class.getCanonicalName();
    /** Camera width. Width of part of the screen which is currently viewed by user */
    public static final int sCameraWidth = 800;
    /** Camera height. Height of part of the screen which is currently viewed by user */
    public static final int sCameraHeight = 480;
    public static final int MONEY_UPDATE_TIME = 3;
    public static final int PLANET_RADIUS = 32;
    /** {@link FixtureDef} for obstacles (static bodies) */
    private final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
    /*
     * ITexture definitions
     */
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
    /** object, which display money status to user */
    private Text mMoneyText;
    /** hold all texture regions used in current game */
    private TextureRegionHolderUtils mTextureRegionHolderUtils;
    /** text which displaying to user with money amount */
    private String mMoneyTextPrefixString;

    @Override
    public EngineOptions onCreateEngineOptions() {
        // init camera
        mCamera = new SmoothCamera(0, 0, sCameraWidth, sCameraHeight, sCameraWidth, sCameraHeight, 1.0f);
        mCamera.setBounds(0, 0, sCameraWidth, sCameraHeight);
        mCamera.setBoundsEnabled(true);
        // multi-touch
        if (!MultiTouch.isSupported(this)) {
            LoggerHelper.printErrorMessage(TAG, "MultiTouch isn't supported");
            finish();
        }

        return new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(sCameraWidth, sCameraHeight), mCamera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();

        //* small objects

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(getTextureManager(), 29, 12, TextureOptions.BILINEAR);
        // warriors
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_RED_WARRIOR,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, this, GameStringConstants.FILE_RED_WARRIOR, 0, 0));
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_BLUE_WARRIOR,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, this, GameStringConstants.FILE_BLUE_WARRIOR, 12, 0));
        // buildings
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_FIRST_BUILDING,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, this, GameStringConstants.FILE_FIRST_BUILDING, 24, 0));
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_SECOND_BUILDING,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, this, GameStringConstants.FILE_SECOND_BUILDING, 24, 5));

        // loading
        smallObjectTexture.load();


        //* bigger objects
        BitmapTextureAtlas biggerObjectsTexture = new BitmapTextureAtlas(getTextureManager(), 192, 64, TextureOptions.BILINEAR);
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringConstants.FILE_SUN, 0, 0));
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_RED_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringConstants.FILE_RED_PLANET, 64, 0));
        mTextureRegionHolderUtils.addElement(GameStringConstants.KEY_BLUE_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringConstants.FILE_BLUE_PLANET, 128, 0));
        biggerObjectsTexture.load();


        // font
        IFont font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 12, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(GameStringConstants.KEY_FONT_MONEY, font);

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) {
        mScene = new Scene();

        mScene.setBackground(new Background(0, 0, 0));
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        mScene.registerUpdateHandler(mPhysicsWorld);
        // create Sun
        createStaticObject(sCameraWidth / 2 - PLANET_RADIUS, sCameraHeight / 2 - PLANET_RADIUS, mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_SUN), GameStringConstants.KEY_SUN);
        // create red planet
        mRedTeam = new Team(GameStringConstants.RED_TEAM_NAME, createPlanet(0, sCameraHeight / 2 - PLANET_RADIUS,
                mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_RED_PLANET), GameStringConstants.KEY_RED_PLANET));
        mRedTeam.getTeamPlanet().setSpawnPoint(PLANET_RADIUS + 15, sCameraHeight / 2);
        mTeams.add(mRedTeam);
        // create blue planet
        mBlueTeam = new Team(GameStringConstants.BLUE_TEAM_NAME, createPlanet(sCameraWidth - PLANET_RADIUS * 2,
                sCameraHeight / 2 - PLANET_RADIUS,
                mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_BLUE_PLANET), GameStringConstants.KEY_BLUE_PLANET));
        mBlueTeam.getTeamPlanet().setSpawnPoint(sCameraWidth - PLANET_RADIUS - 15, sCameraHeight / 2);
        mTeams.add(mBlueTeam);

        createBounds();
        initSceneTouch();

        initGameLogicAndRelatedElements();

        onCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public String getStringById(final int stringId) {
        return getStringById(stringId);
    }

    @Override
    public void detachEntity(final IEntity entity) {
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mScene.detachChild(entity);
            }
        });
    }

    @Override
    public void attachEntity(final IEntity entity) {
        mScene.attachChild(entity);
    }

    @Override
    public void attachEntities(final List<IEntity> entities) {
        for(IEntity entity : entities)
            mScene.attachChild(entity);

    }

    @Override
    public void detachEntities(final List<IEntity> entities) {
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                for(IEntity entity : entities)
                    mScene.detachChild(entity);
            }
        });
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
        StaticObject staticObjectSprite = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        PhysicsFactory.createCircleBody(mPhysicsWorld, staticObjectSprite, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
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
        PhysicsFactory.createCircleBody(mPhysicsWorld, planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        mScene.attachChild(planetStaticObject);
        mStaticObjects.put(key, planetStaticObject);
        return planetStaticObject;
    }

    /** should to separate red (your) from blue (pc) logic */
    private void initGameLogicAndRelatedElements() {
        initUser(mRedTeam, mBlueTeam);
        initBot(mBlueTeam, mRedTeam);
        initMoney();
    }

    /** init planet touch listener for some team */
    private void initUser(final ITeam initializingTeam, final ITeam enemyTeam) {
        // create building
        final StaticObject initiatedTeamPlanet = initializingTeam.getTeamPlanet();
//        ISpriteTouchListener initiatedTeamPlanetTouchListener = new ISpriteTouchListener() {
//            @Override
//            public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//                LoggerHelper.methodInvocation(TAG, "initGameLogicAndRelatedElements.planetOnTouchListener");
//                if (!pSceneTouchEvent.isActionUp()) return true;
//
//                ITextureRegion textureRegion = initializingTeam.getTeamName().equals(GameStringConstants.RED_TEAM_NAME) ?
//                        mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_RED_WARRIOR) :
//                        mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_BLUE_WARRIOR);
//                createUnitForTeam(textureRegion, initializingTeam, enemyTeam);
//                return true;
//            }
//        };
        ISpriteTouchListener initiatedTeamPlanetTouchListener = new UserPlanetTouchListener(initializingTeam,
                getVertexBufferObjectManager(), this, this);
        initiatedTeamPlanet.setOnTouchListener(initiatedTeamPlanetTouchListener);
        mScene.registerTouchArea(initiatedTeamPlanet);
    }

    private void initBot(final ITeam initializingTeam, final ITeam enemyTeam) {
    }

    /** init money string for  displaying to user */
    private void initMoney() {
        mMoneyTextPrefixString = getResources().getString(R.string.money_colon);
        mMoneyText = new Text(sCameraWidth / 2 - 25, 20,
                FontHolderUtils.getInstance().getElement(GameStringConstants.KEY_FONT_MONEY),
                "", mMoneyTextPrefixString.length() + 10, getVertexBufferObjectManager());
        mScene.attachChild(mMoneyText);
        mScene.registerUpdateHandler(new TimerHandler(MONEY_UPDATE_TIME, true, new MoneyUpdateCycle(mTeams) {
            @Override
            public void postUpdate() {
                updateMoneyTextOnScreen();
            }
        }));
    }

    private void updateMoneyTextOnScreen() {
        mMoneyText.setText(TeamUtils.getMoneyString(mMoneyTextPrefixString, mRedTeam));
    }

    private void createBounds() {
        LoggerHelper.methodInvocation(TAG, "createBounds");
        PhysicsFactory.createLineBody(mPhysicsWorld, -1, -1, -1, sCameraHeight + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, -1, -1, sCameraWidth + 1, -1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, sCameraWidth + 1, -1, sCameraWidth + 1, sCameraHeight + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, sCameraWidth + 1, sCameraHeight + 1, -1, sCameraHeight + 1, mStaticBodyFixtureDef);
    }

    private void initSceneTouch() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float screenToSceneRatio = metrics.widthPixels / sCameraWidth;
        mScene.setOnSceneTouchListener(new MainSceneTouchListener(mCamera, this, screenToSceneRatio));
    }

    private Unit createUnitForTeam(final ITextureRegion textureRegion, final ITeam unitTeam, final ITeam enemyTeam) {
        Unit warrior = createUnit(unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY(), textureRegion, mScene);
        unitTeam.addObjectToTeam(warrior);
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(enemyTeam));
        warrior.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam, this));
        return warrior;
    }

    /**
     * create dynamic game object (e.g. warrior or some other stuff)
     *
     * @param x abscissa (top left corner) of created dynamic object
     * @param y ordinate (top left corner) of created dynamic object
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit}
     *
     * @return newly created {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit}
     */
    private Unit createUnit(float x, float y, ITextureRegion textureRegion, Scene scene) {
        LoggerHelper.methodInvocation(TAG, "createUnit");
        Unit unit = new HandsAttacker(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        scene.attachChild(unit);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, unit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        unit.setBody(body);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(unit, body, true, true));
        mUnits.add(unit);
        return unit;
    }
}