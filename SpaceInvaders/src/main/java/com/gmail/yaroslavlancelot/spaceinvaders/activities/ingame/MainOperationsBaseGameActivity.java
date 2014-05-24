package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.ai.NormalBot;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.PlanetDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.BuildingsPopupTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.IItemPickListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.BuildingsListItemBackgroundSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionPopupCompositeSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.CollisionCategoriesUtils;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public abstract class MainOperationsBaseGameActivity extends BaseGameActivity implements Localizable, EntityOperations, ContactListener {
    /**
     * tag, which is used for debugging purpose
     */
    public static final String TAG = MainOperationsBaseGameActivity.class.getCanonicalName();
    public static final int MONEY_UPDATE_TIME = 10;
    /** {@link com.badlogic.gdx.physics.box2d.FixtureDef} for obstacles (static bodies) */
    protected final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CollisionCategoriesUtils.CATEGORY_STATIC_OBJECT,
            CollisionCategoriesUtils.MASKBITS_STATIC_OBJECT_THICK, (short) 0);
    /** contains whole game units/warriors */
    private final Map<Long, GameObject> mGameObjectsMap = new HashMap<Long, GameObject>();
    /** game scene */
    protected Scene mGameScene;
    /** all teams in current game */
    protected Map<String, ITeam> mTeams = new HashMap<String, ITeam>();
    /** first team */
    protected ITeam mSecondTeam;
    /** second team */
    protected ITeam mFirstTeam;
    /** text which displaying to user with money amount */
    protected String mMoneyTextPrefixString;
    /** user static area */
    protected HUD mHud;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /* splash screen */
    protected Scene mSplashScene;
    //TODO check is textures depends on race colour
    protected IRace firstTeamUserRace;
    protected IRace secondTeamUserRace;
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

                mPhysicsWorld.setContactListener(MainOperationsBaseGameActivity.this);
                mBackgroundMusic.initBackgroundMusic();
                mBackgroundMusic.playBackgroundMusic();

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
        Sprite splash = new Sprite(0, 0, mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_SPLASH_SCREEN),
                mEngine.getVertexBufferObjectManager());

        splash.setScale(4f);
        splash.setPosition((SizeConstants.GAME_FIELD_WIDTH - splash.getWidth()) * 0.5f,
                (SizeConstants.GAME_FIELD_HEIGHT - splash.getHeight()) * 0.5f);
        mSplashScene.attachChild(splash);
    }

    protected void onLoadGameResources() {
        LoggerHelper.methodInvocation(TAG, "onCreateGameResources");
        // if it's not empty from previous run
        TextureRegionHolderUtils.getInstance().clear();

        // music
        mMusicAndSoundsHandler = new MusicAndSoundsHandler(getSoundManager(), MainOperationsBaseGameActivity.this);
        mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(getMusicManager());

        //races load
        firstTeamUserRace = new Imperials(Color.RED, this, mMusicAndSoundsHandler);
        firstTeamUserRace.loadResources(getTextureManager(), this);
        secondTeamUserRace = new Imperials(Color.BLUE, this, mMusicAndSoundsHandler);
        secondTeamUserRace.loadResources(getTextureManager(), this);

        // other loader
        BuildingsListItemBackgroundSprite.loadResources(this, getTextureManager());
        DescriptionPopupCompositeSprite.loadResources(this, getTextureManager());

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
        if (mMoneyText.getX() < SizeConstants.GAME_FIELD_WIDTH / 2)
            mMoneyText.setX(SizeConstants.MONEY_PADDING);
        else
            mMoneyText.setX(SizeConstants.GAME_FIELD_WIDTH - mMoneyText.getWidth() - SizeConstants.MONEY_PADDING);
    }

    protected void onInitGameScene() {
        mGameScene = new Scene();
        mGameScene.setBackground(new Background(0, 0, 0));

        // sun and planets
        createSun();

        initGameSceneTouch();
        initHud();

        initMoneyTextView();
        mGameScene.registerUpdateHandler(mPhysicsWorld);
    }

    protected void onInitPlanetsAndTeams() {
        initTeams();

        Intent intent = getIntent();

        // first team init
        TeamControlBehaviourType teamBehaviorType = TeamControlBehaviourType.valueOf(intent.getStringExtra(GameStringsConstantsAndUtils.FIRST_TEAM_NAME));
        initFirstPlanet(TeamControlBehaviourType.isClientSide(teamBehaviorType));

        // second team init
        teamBehaviorType = TeamControlBehaviourType.valueOf(intent.getStringExtra(GameStringsConstantsAndUtils.SECOND_TEAM_NAME));
        initSecondPlanet(TeamControlBehaviourType.isClientSide(teamBehaviorType));

        positionizeMoneyText();
    }

    protected void initTeams() {
        // red team
        mSecondTeam = createTeam(GameStringsConstantsAndUtils.SECOND_TEAM_NAME, firstTeamUserRace);
        mFirstTeam = createTeam(GameStringsConstantsAndUtils.FIRST_TEAM_NAME, secondTeamUserRace);

        mSecondTeam.setTeamColor(Color.RED);
        mSecondTeam.setTeamColor(Color.BLUE);

        // set enemies
        mSecondTeam.setEnemyTeam(mFirstTeam);
        mFirstTeam.setEnemyTeam(mSecondTeam);

        initTeam(mFirstTeam);
        initTeam(mSecondTeam);
    }

    /**
     * initialize team (init user or bot team, or do nothing if team control from remote)
     *
     * @param team team to init
     */
    private void initTeam(ITeam team) {
        TeamControlBehaviourType teamType = team.getTeamControlType();
        initTeamFixtureDef(team);

        if (teamType == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE ||
                teamType == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE) {
            initUserControlledTeam(team);
        } else if (teamType == TeamControlBehaviourType.BOT_CONTROL_ON_SERVER_SIDE) {
            initBotControlledTeam(team);
        } else if (teamType == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE ||
                teamType == TeamControlBehaviourType.REMOTE_CONTROL_ON_SERVER_SIDE) {
            //nothing to do here
        } else {
            throw new IllegalArgumentException("unknown team type =" + teamType);
        }

        mTeams.put(team.getTeamName(), team);
    }

    protected void initTeamFixtureDef(ITeam team) {
        TeamControlBehaviourType type = team.getTeamControlType();
        boolean isRemote = TeamControlBehaviourType.isClientSide(type);
        if (team.getTeamName().equals(GameStringsConstantsAndUtils.FIRST_TEAM_NAME)) {
            if (isRemote)
                team.changeFixtureDefFilter(CollisionCategoriesUtils.CATEGORY_TEAM1, CollisionCategoriesUtils.MASKBITS_TEAM1_THIN);
            else
                team.changeFixtureDefFilter(CollisionCategoriesUtils.CATEGORY_TEAM1, CollisionCategoriesUtils.MASKBITS_TEAM1_THICK);
            return;
        }
        if (isRemote)
            team.changeFixtureDefFilter(CollisionCategoriesUtils.CATEGORY_TEAM2, CollisionCategoriesUtils.MASKBITS_TEAM2_THIN);
        else
            team.changeFixtureDefFilter(CollisionCategoriesUtils.CATEGORY_TEAM2, CollisionCategoriesUtils.MASKBITS_TEAM2_THICK);
    }

    protected abstract void userWantCreateBuilding(ITeam userTeam, int buildingId);

    /** create new team depending on team control type which stored in extra */
    protected ITeam createTeam(String teamNameInExtra, IRace race) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));
        Team team;

        if (teamType == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE) {
            team = new Team(teamNameInExtra, race, teamType);
            updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, team));
        }
        if (teamType == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE) {
            team = new Team(teamNameInExtra, race, teamType) {
                @Override
                public void setMoney(final int money) {
                    super.setMoney(money);
                    updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, this));
                }
            };
            updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, team));
        } else {
            team = new Team(teamNameInExtra, race, teamType);
        }
        return team;
    }

    /** init first team and planet */
    protected void initSecondPlanet(boolean isFakePlanet) {
        mSecondTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER
                        - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_RED_PLANET),
                GameStringsConstantsAndUtils.KEY_RED_PLANET,
                mSecondTeam, isFakePlanet
        ));
        mSecondTeam.getTeamPlanet().setSpawnPoint(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 -
                        SizeConstants.UNIT_SIZE - 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.GAME_FIELD_HEIGHT / 2
        );
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, boolean isFakePlanet, long... unitUniqueId) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, this, team, isFakePlanet);
        planetStaticObject.setObjectDestroyedListener(new PlanetDestroyedListener(team, this));
        mStaticObjects.put(key, planetStaticObject);
        attachEntity(planetStaticObject);
        if (unitUniqueId.length > 0)
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        registerCircleBody(planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        return planetStaticObject;
    }

    /** init second team and planet */
    protected void initFirstPlanet(boolean isFakePlanet) {
        mFirstTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2
                        + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET),
                GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                mFirstTeam, isFakePlanet
        ));
        mFirstTeam.getTeamPlanet().setSpawnPoint(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.UNIT_SIZE + 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET);
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
        mGameObjectsMap.put(sunStaticObject.getObjectUniqueId(), sunStaticObject);
        registerCircleBody(sunStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        return sunStaticObject;
    }

    /** init planet touch listener for some team */
    protected void initUserControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initUserControlledTeam");
        // building popup
        mMainSceneTouchListener.addTouchListener(new BuildingsPopupTouchListener(initializingTeam, this, this,
                new IItemPickListener() {
                    @Override
                    public void itemPicked(final int itemId) {
                        DescriptionPopupCompositeSprite.getInstance().show(initializingTeam.getTeamRace().getBuildingById(itemId)); //userWantCreateBuilding(initializingTeam, itemId);
                    }
                }
        ));
    }

    private void positionizeMoneyText() {
        for (ITeam team : mTeams.values()) {
            if (!TeamControlBehaviourType.isUserControlType(team.getTeamControlType())) continue;
            PlanetStaticObject planet = team.getTeamPlanet();
            if (planet.getX() < SizeConstants.GAME_FIELD_WIDTH / 2)
                mMoneyText.setX(SizeConstants.MONEY_PADDING);
            else
                mMoneyText.setX(SizeConstants.GAME_FIELD_WIDTH - mMoneyText.getWidth() - SizeConstants.MONEY_PADDING);
            mMoneyText.setY(SizeConstants.MONEY_FONT_SIZE * 2);

        }
    }

    protected void initBotControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledTeam");
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                LoggerHelper.printDebugMessage(TAG, "bot team == null : " + (initializingTeam == null));
                Callable<Boolean> simpleBot = new NormalBot(initializingTeam);
                Future<Boolean> future = executorService.submit(simpleBot);
            }
        }, 1, TimeUnit.SECONDS);
    }

    /** init money string for  displaying to user */
    private void initMoneyTextView() {
        LoggerHelper.methodInvocation(TAG, "initMoneyTextView");
        mMoneyTextPrefixString = getString(R.string.money_colon);
        int maxStringLength = mMoneyTextPrefixString.length() + 6;
        mMoneyText = new Text(0f, 0f, FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                "", maxStringLength, getVertexBufferObjectManager());
        mHud.attachChild(mMoneyText);
    }

    /** init hud */
    private void initHud() {
        mHud = mCamera.getHUD();
        mHud.setTouchAreaBindingOnActionDownEnabled(true);
        attachEntityToHud(DescriptionPopupCompositeSprite.init(this));
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
    public synchronized void onResumeGame() {
        super.onResumeGame();
        if (mBackgroundMusic != null)
            mBackgroundMusic.playBackgroundMusic();
    }

    @Override
    public synchronized void onPauseGame() {
        super.onPauseGame();
        if (mBackgroundMusic != null)
            mBackgroundMusic.pauseBackgroundMusic();
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
                    mGameObjectsMap.remove(shapeArea);
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
    public void attachEntityToHud(final IAreaShape entity) {
        HUD hud = mCamera.getHUD();
        hud.attachChild(entity);
    }

    @Override
    public void registerHudTouch(IAreaShape entity) {
        HUD hud = mCamera.getHUD();
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

    /** create unit with body and update it's enemies and moving path */
    @Override
    public Unit createThickUnit(int unitKey, final ITeam unitTeam) {
        Unit warrior = createThinUnit(unitKey, unitTeam,
                unitTeam.getTeamPlanet().getSpawnPointX(), unitTeam.getTeamPlanet().getSpawnPointY());
        warrior.registerUpdateHandler();
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        warrior.initMovingPath();
        return warrior;
    }

    /**
     * create unit (with physic body) in particular position and add it to team
     */
    protected Unit createThinUnit(int unitKey, ITeam unitTeam, float x, float y, long... unitUniqueId) {
        Unit unit = unitTeam.getTeamRace().getUnitForBuilding(unitKey);
        unit.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam, this));
        unit.setPosition(x, y);
        unitTeam.addObjectToTeam(unit);

        if (unitUniqueId.length > 0)
            unit.setObjectUniqueId(unitUniqueId[0]);
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);

        // init physic body
        BodyDef.BodyType bodyType = BodyDef.BodyType.DynamicBody;
        registerCircleBody(unit, bodyType, unitTeam.getFixtureDefUnit());
        if (unitTeam.getTeamControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE)
            unit.removeDamage();
        unit.setBulletFixtureDef(CollisionCategoriesUtils.getBulletFixtureDefByUnitCategory(
                unitTeam.getFixtureDefUnit().filter.categoryBits));

        attachEntity(unit);

        return unit;
    }

    @Override
    public Body registerCircleBody(RectangleWithBody gameObject, BodyDef.BodyType pBodyType, FixtureDef pFixtureDef, float... transform) {
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, gameObject, pBodyType, pFixtureDef);
        if (transform != null && transform.length == 3)
            body.setTransform(transform[0], transform[1], transform[2]);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(gameObject, body, true, true));
        gameObject.setBody(body);
        return body;
    }

    protected abstract void initThickClient();

    @Override
    public void detachPhysicsBody(final RectangleWithBody gameObject) {
        MainOperationsBaseGameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Body body = gameObject.removeBody();
                if (body == null) {return;}
                final PhysicsConnector pc = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(gameObject);
                if (pc != null) {
                    mPhysicsWorld.unregisterPhysicsConnector(pc);
                }
                body.setActive(false);
                mPhysicsWorld.destroyBody(body);
            }
        });
    }

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }

    @Override
    public void beginContact(Contact contact) {
        Object firstBody = contact.getFixtureA().getBody().getUserData();
        Object secondBody = contact.getFixtureB().getBody().getUserData();
        if (firstBody == null || secondBody == null) return;
        if (firstBody instanceof Bullet || secondBody instanceof Bullet)
            attackIfBullet(firstBody, secondBody);
    }

    protected void attackIfBullet(Object firstBody, Object secondBody) {
        if (firstBody == null || secondBody == null) return;
        if (firstBody instanceof Bullet && secondBody instanceof Bullet) {
            bulletsCollision((Bullet) firstBody, (Bullet) secondBody);
            return;
        }
        if (firstBody instanceof Bullet) {
            if (!((Bullet) firstBody).getAndSetFalseIsObjectAlive()) return;
            if (secondBody instanceof GameObject)
                ((GameObject) secondBody).damageObject(((Bullet) firstBody).getDamage());
            else
                return;
            detachPhysicsBody((Bullet) firstBody);
            detachEntity((Bullet) firstBody);
        }
        if (secondBody instanceof Bullet) {
            if (!((Bullet) secondBody).getAndSetFalseIsObjectAlive()) return;
            if (firstBody instanceof GameObject)
                ((GameObject) firstBody).damageObject(((Bullet) secondBody).getDamage());
            else
                return;
            detachPhysicsBody((Bullet) secondBody);
            detachEntity((Bullet) secondBody);
        }
    }

    protected void bulletsCollision(Bullet firstBody, Bullet secondBody) {
        if (!firstBody.getAndSetFalseIsObjectAlive() || !secondBody.getAndSetFalseIsObjectAlive())
            return;
        detachPhysicsBody(firstBody);
        detachPhysicsBody(secondBody);
        detachEntity(firstBody);
        detachEntity(secondBody);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}