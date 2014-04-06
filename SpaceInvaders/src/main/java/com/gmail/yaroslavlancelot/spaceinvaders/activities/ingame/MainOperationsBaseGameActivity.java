package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import android.content.Intent;
import android.graphics.Typeface;
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
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.PlanetDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.BuildingsPopupTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.IItemPickListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitCallbacksUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.Localizable;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public abstract class MainOperationsBaseGameActivity extends BaseGameActivity implements Localizable, EntityOperations {
    /**
     * tag, which is used for debugging purpose
     */
    public static final String TAG = MainOperationsBaseGameActivity.class.getCanonicalName();
    public static final int MONEY_UPDATE_TIME = 10;
    /** contains whole game units/warriors */
    private final Map<Long, Unit> mUnitsMap = new HashMap<Long, Unit>();
    /** game scene */
    protected Scene mGameScene;
    /** all teams in current game */
    protected Map<String, ITeam> mTeams = new HashMap<String, ITeam>();
    /** red team */
    protected ITeam mRedTeam;
    /** blue team */
    protected ITeam mBlueTeam;
    /** text which displaying to user with money amount */
    protected String mMoneyTextPrefixString;
    /** user static area */
    protected HUD mHud;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /* splash screen */
    protected Scene mSplashScene;
    //TODO check is textures depends on race colour
    protected IRace redTeamUserRace;
    protected IRace blueTeamUserRace;
    /** contains game obstacles and other static objects */
    private HashMap<String, StaticObject> mStaticObjects = new HashMap<String, StaticObject>();
    /** game camera */
    private SmoothCamera mCamera;
    /** object, which display money status to user */
    private Text mMoneyText;
    /** hold all texture regions used in current game */
    private TextureRegionHolderUtils mTextureRegionHolderUtils;
    /** main scene touch listener */
    private MainSceneTouchListener mMainSceneTouchListener;
    /** background theme */
    private MusicAndSoundsHandler mMusicAndSoundsHandler;
    private MusicAndSoundsHandler.BackgroundMusic mBackgroundMusic;
    private Sprite mSplash;

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
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT), mCamera
        );

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);

        // music

        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);

        return engineOptions;
    }

    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                onLoadGameResources();
                onInitGameScene();
                initThickClient();
                onInitPlanetsAndTeams();

                mBackgroundMusic.startBackgroundMusic();

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);
            }
        }));
    }


    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateResources");

        mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();

        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 32, TextureOptions.DEFAULT);
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_SPLASH_SCREEN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        splashTextureAtlas, this, GameStringsConstantsAndUtils.FILE_SPLASH_SCREEN, 0, 0)
        );
        splashTextureAtlas.load();

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback onCreateSceneCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateScene");

        initSplashScene();
        onCreateSceneCallback.onCreateSceneFinished(mSplashScene);
    }

    protected void initSplashScene() {
        mSplashScene = new Scene();
        mSplash = new Sprite(0, 0, mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_SPLASH_SCREEN),
                mEngine.getVertexBufferObjectManager());

        mSplash.setScale(4f);
        mSplash.setPosition((SizeConstants.GAME_FIELD_WIDTH - mSplash.getWidth()) * 0.5f,
                (SizeConstants.GAME_FIELD_HEIGHT - mSplash.getHeight()) * 0.5f);
        mSplashScene.attachChild(mSplash);
    }

    protected void onLoadGameResources() {
        LoggerHelper.methodInvocation(TAG, "onCreateGameResources");
        // music
        mMusicAndSoundsHandler = new MusicAndSoundsHandler(getSoundManager(), MainOperationsBaseGameActivity.this);
        mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(getMusicManager());

        //races load
        redTeamUserRace = new Imperials(Color.RED, this, mMusicAndSoundsHandler);
        redTeamUserRace.loadResources(getTextureManager(), this);
        blueTeamUserRace = new Imperials(Color.BLUE, this, mMusicAndSoundsHandler);
        blueTeamUserRace.loadResources(getTextureManager(), this);

        //* bigger objects
        BitmapTextureAtlas biggerObjectsTexture = new BitmapTextureAtlas(getTextureManager(),
                512, 512, TextureOptions.BILINEAR);
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_SUN, 0, 0));
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_RED_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_RED_PLANET,
                        0, SizeConstants.FILE_SUN_DIAMETER)
        );
        mTextureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, this, GameStringsConstantsAndUtils.FILE_BLUE_PLANET,
                        SizeConstants.PLANET_DIAMETER, SizeConstants.FILE_SUN_DIAMETER)
        );
        biggerObjectsTexture.load();

        // font
        IFont font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY, font);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();

        changeSplashSceneWithGameScene();
    }

    /**
     * update money amount
     */
    protected void updateMoneyTextOnScreen(String value) {
        mMoneyText.setText(value);
    }

    protected void onInitGameScene() {
        mGameScene = new Scene();
        mGameScene.setBackground(new Background(0, 0, 0));

        // sun and planets
        createSun();

        initGameSceneTouch();

        initMoneyTextView();
        mGameScene.registerUpdateHandler(mPhysicsWorld);
    }

    protected void onInitPlanetsAndTeams() {
        initTeams();

        Intent intent = getIntent();

        // first team init
        TeamControlBehaviourType team = TeamControlBehaviourType.valueOf(intent.getStringExtra(GameStringsConstantsAndUtils.RED_TEAM_NAME));
        initRedPlanet(team == TeamControlBehaviourType.REMOTE_CLIENT_CONTROL || team == TeamControlBehaviourType.USER_CLIENT_CONTROL);

        // second team init
        team = TeamControlBehaviourType.valueOf(intent.getStringExtra(GameStringsConstantsAndUtils.BLUE_TEAM_NAME));
        initBluePlanet(team == TeamControlBehaviourType.REMOTE_CLIENT_CONTROL || team == TeamControlBehaviourType.USER_CLIENT_CONTROL);
    }

    protected void initTeams() {
        // red team
        mRedTeam = createTeam(GameStringsConstantsAndUtils.RED_TEAM_NAME, redTeamUserRace);
        mBlueTeam = createTeam(GameStringsConstantsAndUtils.BLUE_TEAM_NAME, blueTeamUserRace);

        mRedTeam.setTeamColor(Color.RED);
        mRedTeam.setTeamColor(Color.BLUE);

        // set enemies
        mRedTeam.setEnemyTeam(mBlueTeam);
        mBlueTeam.setEnemyTeam(mRedTeam);

        initTeam(mBlueTeam, GameStringsConstantsAndUtils.BLUE_TEAM_NAME);
        initTeam(mRedTeam, GameStringsConstantsAndUtils.RED_TEAM_NAME);
    }

    /**
     * initialize team (init user or bot team, or do nothing if team control from remote)
     *
     * @param team            team to init
     * @param teamNameInExtra team name to get control type from intent
     */
    private void initTeam(ITeam team, String teamNameInExtra) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));

        if (teamType == TeamControlBehaviourType.USER_SERVER_CONTROL ||
                teamType == TeamControlBehaviourType.USER_CLIENT_CONTROL) {
            initUserControlledTeam(team);
        } else if (teamType == TeamControlBehaviourType.BOT_CONTROL) {
            initBotControlledTeam(team);
        } else if (teamType == TeamControlBehaviourType.REMOTE_CLIENT_CONTROL ||
                teamType == TeamControlBehaviourType.REMOTE_SERVER_CONTROL) {
            //nothing
        } else {
            throw new IllegalArgumentException("unknown team type =" + teamType);
        }

        mTeams.put(team.getTeamName(), team);
    }

    protected abstract void userWantCreateBuilding(ITeam userTeam, int buildingId);

    /** create new team depending on team control type which stored in extra */
    private ITeam createTeam(String teamNameInExtra, IRace race) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));

        if (teamType == TeamControlBehaviourType.USER_SERVER_CONTROL) {
            return new Team(teamNameInExtra, race, teamType) {
                @Override
                public void changeMoney(final int delta) {
                    super.changeMoney(delta);
                    updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, this));
                }
            };
        } else {
            return new Team(teamNameInExtra, race, teamType);
        }
    }

    /** init red team and planet */
    protected void initRedPlanet(boolean isFakePlanet) {
        mRedTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2
                        + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_RED_PLANET), GameStringsConstantsAndUtils.KEY_RED_PLANET,
                mRedTeam, isFakePlanet
        ));
        mRedTeam.getTeamPlanet().setSpawnPoint(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.UNIT_SIZE + 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET);
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, boolean isFakePlanet) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, this, team, isFakePlanet);
        planetStaticObject.setObjectDestroyedListener(new PlanetDestroyedListener(team, this));
        mStaticObjects.put(key, planetStaticObject);
        attachEntity(planetStaticObject);
        return planetStaticObject;
    }

    /** init blue team and planet */
    protected void initBluePlanet(boolean isFakePlanet) {
        mBlueTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER
                        - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET), GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                mBlueTeam, isFakePlanet
        ));
        mBlueTeam.getTeamPlanet().setSpawnPoint(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 -
                        SizeConstants.UNIT_SIZE - 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.GAME_FIELD_HEIGHT / 2
        );
    }

    /** create sun */
    protected SunStaticObject createSun() {
        float x = (SizeConstants.GAME_FIELD_WIDTH - SizeConstants.SUN_DIAMETER) / 2;
        float y = (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.SUN_DIAMETER) / 2;
        ITextureRegion textureRegion = mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_SUN);
        String key = GameStringsConstantsAndUtils.KEY_SUN;

        SunStaticObject sunStaticObject = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        mStaticObjects.put(key, sunStaticObject);
        attachEntity(sunStaticObject);
        return sunStaticObject;
    }

    /** init planet touch listener for some team */
    protected void initUserControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initUserControlledTeam");
        // create building
        ITouchListener userClickScreenTouchListener = new BuildingsPopupTouchListener(initializingTeam, this, this,
                new IItemPickListener() {
                    @Override
                    public void itemPicked(final int itemId) {
                        userWantCreateBuilding(initializingTeam, itemId);
                    }
                }
        );
        mMainSceneTouchListener.addTouchListener(userClickScreenTouchListener);
    }

    protected void initBotControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledTeam");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        LoggerHelper.printDebugMessage(TAG, "bot team == null : " + (initializingTeam == null));
        Callable<Boolean> simpleBot = new NormalBot(initializingTeam);
        Future<Boolean> future = executorService.submit(simpleBot);
    }

    /** init money string for  displaying to user */
    private void initMoneyTextView() {
        LoggerHelper.methodInvocation(TAG, "initMoneyTextView");
        mMoneyTextPrefixString = getString(R.string.money_colon);
        int maxStringLength = mMoneyTextPrefixString.length() + 6;
        mMoneyText = new Text(SizeConstants.GAME_FIELD_WIDTH - maxStringLength * SizeConstants.MONEY_FONT_SIZE,
                SizeConstants.MONEY_FONT_SIZE, FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                "", maxStringLength, getVertexBufferObjectManager());
        mHud = mCamera.getHUD();
        mHud.attachChild(mMoneyText);
    }

    /** init scene touch events so user can collaborate with game by screen touches */
    private void initGameSceneTouch() {
        LoggerHelper.methodInvocation(TAG, "initGameSceneTouch");
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float screenToSceneRatio = metrics.widthPixels / SizeConstants.GAME_FIELD_WIDTH;
        mMainSceneTouchListener = new MainSceneTouchListener(mCamera, this, screenToSceneRatio);
        mGameScene.setOnSceneTouchListener(mMainSceneTouchListener);
        mMusicAndSoundsHandler.setCameraCoordinates(mMainSceneTouchListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBackgroundMusic != null)
            mBackgroundMusic.startBackgroundMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBackgroundMusic != null)
            mBackgroundMusic.stopBackgroundMusic();
    }

    @Override
    public String getStringById(final int stringId) {
        return getString(stringId);
    }

    @Override
    public void detachEntity(final IAreaShape shapeArea) {
        MainOperationsBaseGameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mGameScene.unregisterTouchArea(shapeArea);
                mGameScene.detachChild(shapeArea);
                if (shapeArea instanceof Unit)
                    mUnitsMap.remove(shapeArea);
            }
        });
    }

    @Override
    public void attachEntity(final IEntity entity) {
        mGameScene.attachChild(entity);
    }

    @Override
    public void attachEntityWithTouchArea(final IAreaShape entity) {
        mGameScene.attachChild(entity);
        mGameScene.registerTouchArea(entity);
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
        MainOperationsBaseGameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                hud.unregisterTouchArea(entity);
                hud.detachChild(entity);
            }
        });
    }

    @Override
    public VertexBufferObjectManager getObjectManager() {
        return getVertexBufferObjectManager();
    }

    /** create unit and assign it for particular team */
    @Override
    public Unit createUnitForTeam(int unitKey, final ITeam unitTeam) {
        Unit warrior = createUnit(unitKey, unitTeam,
                unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY());
        warrior.registerUpdateHandler();
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        warrior.initMovingPath();
        unitTeam.addObjectToTeam(warrior);
        return warrior;
    }

    /**
     * create new unit with by uniqueId for race which will be retrieved from the unitTeam.
     * unitUniqueId will replace default value if present (will take first value if there will be few of them)
     */
    protected Unit createUnit(int unitKey, ITeam unitTeam, float x, float y, long... unitUniqueId) {
        Unit unit = unitTeam.getTeamRace().getUnitForBuilding(unitKey);
        unit.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam, this));
        unit.setPosition(x, y);
        attachEntity(unit);
        if (unitUniqueId.length > 0)
            unit.setUnitUniqueId(unitUniqueId[0]);
        mUnitsMap.put(unit.getUnitUniqueId(), unit);

        // init physic body
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        BodyDef.BodyType bodyType;
        if (unitTeam.getTeamControlType() == TeamControlBehaviourType.REMOTE_CLIENT_CONTROL)
            bodyType = BodyDef.BodyType.KinematicBody;
        else
            bodyType = BodyDef.BodyType.DynamicBody;
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, unit, bodyType, playerFixtureDef);
        unit.setBody(body);
        body.setUserData(unit);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(unit, body, true, true));

        return unit;
    }

    protected abstract void initThickClient();

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

    /** return unit if it exist (live) by using unit unique id */
    protected Unit getUnitById(long id) {
        return mUnitsMap.get(id);
    }
}