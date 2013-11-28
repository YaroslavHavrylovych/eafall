package com.gmail.yaroslavlancelot.spaceinvaders.game;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Display;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.ai.NormalBot;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.MoneyUpdateCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.PlanetDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.BuildingsPopupShowListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.SoundsAndMusicUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitCallbacksUtils;
import org.andengine.audio.music.Music;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public class GameActivity extends BaseGameActivity implements Localizable, EntityOperations, MediaPlayer.OnPreparedListener {
    /** tag, which is used for debugging purpose */
    public static final String TAG = GameActivity.class.getCanonicalName();
    public static final int MONEY_UPDATE_TIME = 10;
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
    private ITeam mRedTeam;
    /** blue team */
    private ITeam mBlueTeam;
    /** game camera */
    private SmoothCamera mCamera;
    /** object, which display money status to user */
    private Text mMoneyText;
    /** hold all texture regions used in current game */
    private TextureRegionHolderUtils mTextureRegionHolderUtils;
    /** text which displaying to user with money amount */
    private String mMoneyTextPrefixString;
    /** main scene touch listener */
    private MainSceneTouchListener mMainSceneTouchListener;
    /** background theme */
    private Music mBackgroundMusic;

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
        mCamera.setHUD(new HUD());

        EngineOptions engineOptions = new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT), mCamera);

        // music
        engineOptions.getAudioOptions().setNeedsMusic(true);

        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateResources");
        mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();

        // user
        Color teamColor = Color.RED;
        IRace userRace = new Imperials(getVertexBufferObjectManager(), teamColor, this);
        userRace.loadResources(getTextureManager(), this);
        mRedTeam = createUserTeam(teamColor, userRace, GameStringsConstantsAndUtils.RED_TEAM_NAME);
        // bot
        teamColor = Color.BLUE;
        IRace botRace = new Imperials(getVertexBufferObjectManager(), teamColor, this);
        botRace.loadResources(getTextureManager(), this);
        mBlueTeam = createBotTeam(teamColor, botRace, GameStringsConstantsAndUtils.BLUE_TEAM_NAME);

        //* bigger objects
        BitmapTextureAtlas biggerObjectsTexture = new BitmapTextureAtlas(getTextureManager(),
                512, 512, TextureOptions.BILINEAR);
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_SUN, 0, 0));
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_RED_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_RED_PLANET,
                        0, SizeConstants.FILE_SUN_DIAMETER));
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_BLUE_PLANET,
                        SizeConstants.PLANET_DIAMETER, SizeConstants.FILE_SUN_DIAMETER));
        biggerObjectsTexture.load();


        // font
        IFont font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY, font);

        // music
        mBackgroundMusic = SoundsAndMusicUtils.getMusic(GameStringsConstantsAndUtils.getPathToBackgroundMusic() + "background_1.ogg",
                this, mEngine.getMusicManager());
        if (mBackgroundMusic != null) {
            mBackgroundMusic.setLooping(true);
            mBackgroundMusic.getMediaPlayer().setOnPreparedListener(this);
        }

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    private ITeam createUserTeam(Color teamColor, IRace teamRace, String teamName) {
        ITeam team = new Team(teamName, teamRace) {
            @Override
            public void changeMoney(final int delta) {
                super.changeMoney(delta);
                updateMoneyTextOnScreen();
            }
        };
        team.setTeamColor(teamColor);
        return team;
    }

    private void updateMoneyTextOnScreen() {
        mMoneyText.setText(TeamUtils.getMoneyString(mMoneyTextPrefixString, mRedTeam));
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
        initRedTeamAndPlanet();
        initBlueTeamAndPlanet();

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

    private ITeam createBotTeam(Color teamColor, IRace teamRace, String teamName) {
        ITeam team = new Team(teamName, teamRace);
        team.setTeamColor(teamColor);
        return team;
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        if (mBackgroundMusic != null)
            mBackgroundMusic.getMediaPlayer().prepareAsync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBackgroundMusic != null)
            mBackgroundMusic.stop();
    }

    private void initRedTeamAndPlanet() {
        mRedTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2
                + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_RED_PLANET), GameStringsConstantsAndUtils.KEY_RED_PLANET,
                mRedTeam));
        mRedTeam.getTeamPlanet().setSpawnPoint(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.UNIT_SIZE + 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET);
        mTeams.add(mRedTeam);
    }

    private void initBlueTeamAndPlanet() {
        mBlueTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER
                - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET), GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                mBlueTeam));
        mBlueTeam.getTeamPlanet().setSpawnPoint(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 -
                SizeConstants.UNIT_SIZE - 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
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
        ITextureRegion textureRegion = mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_SUN);
        String key = GameStringsConstantsAndUtils.KEY_SUN;

        LoggerHelper.methodInvocation(TAG, "createSun");
        SunStaticObject sunStaticObject = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        sunStaticObject.setBody(PhysicsFactory.createCircleBody(mPhysicsWorld, sunStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef));
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
        planetStaticObject.setObjectDestroyedListener(new PlanetDestroyedListener(team, this));
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        planetStaticObject.setBody(body);
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
        ITouchListener userClickScreenTouchListener = new BuildingsPopupShowListener(initializingTeam, this, this);
        mMainSceneTouchListener.addTouchListener(userClickScreenTouchListener);
    }

    @SuppressWarnings("unused")
    private void initBot(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBot");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> simpleBot = new NormalBot(initializingTeam);
        Future<Boolean> future = executorService.submit(simpleBot);
    }

    /** init money string for  displaying to user */
    private void initMoney() {
        LoggerHelper.methodInvocation(TAG, "initMoney");
        mMoneyTextPrefixString = getString(R.string.money_colon);
        int maxStringLength = mMoneyTextPrefixString.length() + 6;
        mMoneyText = new Text(SizeConstants.GAME_FIELD_WIDTH - maxStringLength * SizeConstants.MONEY_FONT_SIZE,
                SizeConstants.MONEY_FONT_SIZE, FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                "", maxStringLength, getVertexBufferObjectManager());
        HUD hud = mCamera.getHUD();
        hud.attachChild(mMoneyText);
        updateMoneyTextOnScreen();
        hud.registerUpdateHandler(new TimerHandler(MONEY_UPDATE_TIME, true, new MoneyUpdateCycle(mTeams)));
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
        mMainSceneTouchListener = new MainSceneTouchListener(mCamera, this, screenToSceneRatio);
        mScene.setOnSceneTouchListener(mMainSceneTouchListener);
    }

    @Override
    public String getStringById(final int stringId) {
        return getString(stringId);
    }

    @Override
    public void detachEntity(final IAreaShape shapeArea) {
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mScene.unregisterTouchArea(shapeArea);
                mScene.detachChild(shapeArea);
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
    public void attachEntityWithTouchToHud(final IAreaShape entity) {
        HUD hud = mCamera.getHUD();
        hud.attachChild(entity);
        hud.registerTouchArea(entity);
    }

    @Override
    public void detachEntityFromHud(final IAreaShape entity) {
        final HUD hud = mCamera.getHUD();
        GameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                hud.unregisterTouchArea(entity);
                hud.detachChild(entity);
            }
        });
    }

    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        if (gameObject.getBody() == null)
            return;
        final PhysicsConnector pc = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(gameObject);
        if (pc != null) {
            mPhysicsWorld.unregisterPhysicsConnector(pc);
        }
        mPhysicsWorld.destroyBody(gameObject.getBody());
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
        Unit warrior = createUnitCarcass(unitKey, unitTeam);
        unitTeam.addObjectToTeam(warrior);
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        warrior.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam, this));
        return warrior;
    }

    /**
     * create dynamic game object (e.g. warrior or some other stuff)
     *
     * @param unitKey key to identify which kind of unit you want to build
     * @param unitTeam team unit of which should be created
     *
     * @return newly created unit
     */
    private Unit createUnitCarcass(int unitKey, ITeam unitTeam) {
        LoggerHelper.methodInvocation(TAG, "createUnitCarcass");
        Unit unit = unitTeam.getTeamRace().getUnitForBuilding(unitKey);
        unit.setX(unitTeam.getTeamPlanet().getSpawnPointX());
        unit.setY(unitTeam.getTeamPlanet().getSpawnPointY());
        unit.calculateUnitPath();
        attachEntity(unit);
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, unit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        unit.setBody(body);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(unit, body, true, true));
        mUnits.add(unit);
        return unit;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        mBackgroundMusic.play();
    }
}

