package com.gmail.yaroslavlancelot.spaceinvaders.game;

import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.ai.SimpleBot;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.MoneyUpdateCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.UnitFactory;
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
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
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
    public static final int MONEY_UPDATE_TIME = 3;
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
        LoggerHelper.methodInvocation(TAG, "onCreateEngineOptions");
        // multi-touch
        if (!MultiTouch.isSupported(this)) {
            LoggerHelper.printErrorMessage(TAG, "MultiTouch isn't supported");
            finish();
        }

        // init camera
        mCamera = new SmoothCamera(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT, 1.0f);
        mCamera.setBounds(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        mCamera.setBoundsEnabled(true);

        return new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT), mCamera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateResources");
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
        LoggerHelper.methodInvocation(TAG, "onCreateScene");
        mScene = new Scene();

        mScene.setBackground(new Background(0, 0, 0));
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        mScene.registerUpdateHandler(mPhysicsWorld);
        // sun and planets
        createSun();
        createRedTeamAndPlanet();
        createBlueTeamAndPlanet();

        // set enemies
        mRedTeam.setEnemyTeam(mBlueTeam);
        mBlueTeam.setEnemyTeam(mRedTeam);

        createBounds();
        initSceneTouch();

        initGameLogicAndRelatedElements();

        onCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    private void createRedTeamAndPlanet() {
        mRedTeam = new Team(GameStringConstants.RED_TEAM_NAME);
        mRedTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_RED_PLANET), GameStringConstants.KEY_RED_PLANET,
                mRedTeam));
        mRedTeam.getTeamPlanet().setSpawnPoint(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.UNIT_SIZE + 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2);
        mTeams.add(mRedTeam);
    }

    private void createBlueTeamAndPlanet() {
        mBlueTeam = new Team(GameStringConstants.BLUE_TEAM_NAME);
        mBlueTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_BLUE_PLANET), GameStringConstants.KEY_BLUE_PLANET,
                mBlueTeam));
        mBlueTeam.getTeamPlanet().setSpawnPoint(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 -
                SizeConstants.UNIT_SIZE - 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2);
        mTeams.add(mBlueTeam);
    }

    /**
     * create static game object
     *
     * @return newly created {@link SunStaticObject}
     */
    private SunStaticObject createSun() {
        float x = (SizeConstants.GAME_FIELD_WIDTH - SizeConstants.SUN_DIAMETER) / 2;
        float y = (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.SUN_DIAMETER) / 2;
        ITextureRegion textureRegion = mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_SUN);
        String key = GameStringConstants.KEY_SUN;

        LoggerHelper.methodInvocation(TAG, "createSun");
        SunStaticObject sunStaticObject = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        PhysicsFactory.createCircleBody(mPhysicsWorld, sunStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        attachEntity(sunStaticObject);
        mStaticObjects.put(key, sunStaticObject);
        return sunStaticObject;
    }

    /**
     * create planet game object
     *
     * @param x abscissa (top left corner) of created planet
     * @param y ordinate (top left corner) of created planet
     * @param textureRegion static object {@link ITextureRegion} for creating new {@link PlanetStaticObject}
     * @param key key of current planet
     * @param team new planet team
     *
     * @return newly created {@link PlanetStaticObject}
     */
    private PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, this, team);
        PhysicsFactory.createCircleBody(mPhysicsWorld, planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        attachEntity(planetStaticObject);
        mStaticObjects.put(key, planetStaticObject);
        return planetStaticObject;
    }

    /** should to separate red (your) from blue (pc) logic */
    private void initGameLogicAndRelatedElements() {
        LoggerHelper.methodInvocation(TAG, "initGameLogicAndRelatedElements");
        initUser(mRedTeam);
        initBot(mBlueTeam);
        initMoney();
    }

    /** init planet touch listener for some team */
    private void initUser(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initUser");
        // create building
        final StaticObject initiatedTeamPlanet = initializingTeam.getTeamPlanet();
        ISpriteTouchListener initiatedTeamPlanetTouchListener
                = new UserPlanetTouchListener(initializingTeam, this, this);
        initiatedTeamPlanet.setOnTouchListener(initiatedTeamPlanetTouchListener);
        mScene.registerTouchArea(initiatedTeamPlanet);
    }

    private void initBot(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBot");
        new Thread(new SimpleBot(initializingTeam)).start();
    }

    /** init money string for  displaying to user */
    private void initMoney() {
        LoggerHelper.methodInvocation(TAG, "initMoney");
        mMoneyTextPrefixString = getString(R.string.money_colon);
        mMoneyText = new Text(SizeConstants.GAME_FIELD_WIDTH / 2 - 25, 20,
                FontHolderUtils.getInstance().getElement(GameStringConstants.KEY_FONT_MONEY),
                "", mMoneyTextPrefixString.length() + 10, getVertexBufferObjectManager());
        attachEntity(mMoneyText);
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
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, SizeConstants.GAME_FIELD_WIDTH + 1, -1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1, -1, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_WIDTH + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_HEIGHT + 1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
    }

    private void initSceneTouch() {
        LoggerHelper.methodInvocation(TAG, "initSceneTouch");
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float screenToSceneRatio = metrics.widthPixels / SizeConstants.GAME_FIELD_WIDTH;
        mScene.setOnSceneTouchListener(new MainSceneTouchListener(mCamera, this, screenToSceneRatio));
    }

    @Override
    public String getStringById(final int stringId) {
        return getString(stringId);
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
    public void attachEntityWithTouchArea(final IAreaShape entity) {
        mScene.attachChild(entity);
        mScene.registerTouchArea(entity);
    }

    @Override
    public void detachEntityWithTouch(final IAreaShape entity) {
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mScene.unregisterTouchArea(entity);
                mScene.detachChild(entity);
            }
        });
    }

    @Override
    public VertexBufferObjectManager getObjectManager() {
        return getVertexBufferObjectManager();
    }

    /**
     * create dynamic game object (e.g. warrior or some other stuff)
     *
     * @param unitKey key to identify which kind of unit you want to build
     * @param unitTeam team unit of which should be created
     *
     * @return newly created unit
     */
    @Override
    public Unit createUnitForTeam(int unitKey, final ITeam unitTeam) {
        Unit warrior = createUnit(unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY(),
                unitKey, unitTeam.getTeamName());
        unitTeam.addObjectToTeam(warrior);
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        warrior.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam, this));
        return warrior;
    }

    /**
     * create dynamic game object (e.g. warrior or some other stuff)
     *
     * @param x abscissa (top left corner) of created dynamic object
     * @param y ordinate (top left corner) of created dynamic object
     * @param unitKey key to identify which kind of unit you want to build
     * @param unitTeamName name of team, unit of which should be created
     *
     * @return newly created {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit}
     */
    private Unit createUnit(float x, float y, int unitKey, String unitTeamName) {
        LoggerHelper.methodInvocation(TAG, "createUnit");
        Unit unit = UnitFactory.getInstance(unitKey, unitTeamName, x, y, getVertexBufferObjectManager());
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        attachEntity(unit);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, unit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        unit.setBody(body);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(unit, body, true, true));
        mUnits.add(unit);
        return unit;
    }
}