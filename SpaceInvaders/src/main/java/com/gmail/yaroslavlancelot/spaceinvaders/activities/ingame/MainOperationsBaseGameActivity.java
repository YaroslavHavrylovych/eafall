package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import android.content.Intent;
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
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateCircleBodyEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateUnitEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.GameLoadedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.AbstractEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.AttachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.DetachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.GameObjectsContactListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.PlanetDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.MainSceneTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PathChoosePopup;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.ShowBuildingsPopupButtonSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.CollisionCategoriesConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitCallbacksUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.MoneyText;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public abstract class MainOperationsBaseGameActivity extends BaseGameActivity {
    /**
     * tag, which is used for debugging purpose
     */
    public static final String TAG = MainOperationsBaseGameActivity.class.getCanonicalName();
    /** contains whole game units/warriors */
    private final Map<Long, GameObject> mGameObjectsMap = new HashMap<Long, GameObject>();
    /** game scene */
    protected Scene mGameScene;
    /** first team */
    protected ITeam mSecondTeam;
    /** second team */
    protected ITeam mFirstTeam;
    /** user static area */
    protected HUD mHud;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /** game objects contact listener */
    protected GameObjectsContactListener mContactListener;
    /* splash screen */
    protected Scene mSplashScene;
    /** contains game obstacles and other static objects */
    private HashMap<String, StaticObject> mStaticObjects = new HashMap<String, StaticObject>();
    /** game camera */
    private SmoothCamera mCamera;
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

        GameObject.clearCounter();

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

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();

        changeSplashSceneWithGameScene();
    }

    protected void changeSplashSceneWithGameScene() {
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                onLoadGameResources();
                onInitGameScene();
                initThickClient();
                onInitPlanetsAndTeams();

                mPhysicsWorld.setContactListener(mContactListener = new GameObjectsContactListener());
                mBackgroundMusic.initBackgroundMusic();
                mBackgroundMusic.playBackgroundMusic();

                mSplashScene.detachSelf();
                mEngine.setScene(mGameScene);

                EventBus.getDefault().register(MainOperationsBaseGameActivity.this);
                EventBus.getDefault().post(new GameLoadedEvent());
            }
        }));
    }

    protected void onLoadGameResources() {
        LoggerHelper.methodInvocation(TAG, "onCreateGameResources");

        // music
        mMusicAndSoundsHandler = new MusicAndSoundsHandler(getSoundManager(), MainOperationsBaseGameActivity.this);
        mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(getMusicManager());

        //races loadGeneralGameTextures
        RacesHolder.getInstance().addElement(Imperials.RACE_NAME, new Imperials(getVertexBufferObjectManager(), mMusicAndSoundsHandler));
        for (IRace race : RacesHolder.getInstance().getElements()) {
            race.loadResources(getTextureManager(), this);
        }

        // other loader
        BuildingsPopupHud.loadResource(this, getTextureManager());
        ShowBuildingsPopupButtonSprite.loadResources(this, getTextureManager());
        DescriptionPopupHud.loadResources(this, getTextureManager());
        PathChoosePopup.loadResources(this, getTextureManager());

        TextureRegionHolderUtils.loadGeneralGameTextures(this, getTextureManager());

        // font
        FontHolderUtils.loadGeneralGameFonts(getFontManager(), getTextureManager());
        DescriptionPopupHud.loadFonts(getFontManager(), getTextureManager());
    }

    protected void onInitGameScene() {
        mGameScene = new Scene();
        mGameScene.setBackground(new Background(0, 0, 0));

        // sun and planets
        createSun();

        initGameSceneTouch();
        initHud();

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

        initMoneyText();
        initDescriptionPopup();
        initServerUserPopups();
    }

    private void initDescriptionPopup() {
        DescriptionPopupHud descriptionPopupHud = new DescriptionPopupHud(mHud, getVertexBufferObjectManager());
        descriptionPopupHud.setCamera(mCamera);
    }

    private void initServerUserPopups() {
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            if (team.getTeamControlType() == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE ||
                    team.getTeamControlType() == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE) {
                //buildings
                final BuildingsPopupHud buildingsPopupHud = new BuildingsPopupHud(team.getTeamName(), mHud, getVertexBufferObjectManager());
                buildingsPopupHud.setCamera(mCamera);
                ShowBuildingsPopupButtonSprite button = new ShowBuildingsPopupButtonSprite(getVertexBufferObjectManager());
                button.setOnClickListener(new ButtonSprite.OnClickListener() {
                    @Override
                    public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                        buildingsPopupHud.triggerPopup();
                    }
                });
                mHud.attachChild(button);
                mHud.registerTouchArea(button);
                //path chooser
                PathChoosePopup popup = new PathChoosePopup(mHud, getVertexBufferObjectManager());
                popup.setCamera(mCamera);
            }
        }
    }


    protected void initTeams() {
        // red team
        IRace race = RacesHolder.getInstance().getElement(Imperials.RACE_NAME);
        mSecondTeam = createTeam(GameStringsConstantsAndUtils.SECOND_TEAM_NAME, race);
        mFirstTeam = createTeam(GameStringsConstantsAndUtils.FIRST_TEAM_NAME, race);

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

        if (teamType == TeamControlBehaviourType.BOT_CONTROL_ON_SERVER_SIDE) {
            initBotControlledTeam(team);
        }

        TeamsHolder.getInstance().addElement(team.getTeamName(), team);
    }

    protected void initTeamFixtureDef(ITeam team) {
        TeamControlBehaviourType type = team.getTeamControlType();
        boolean isRemote = TeamControlBehaviourType.isClientSide(type);
        if (team.getTeamName().equals(GameStringsConstantsAndUtils.FIRST_TEAM_NAME)) {
            if (isRemote)
                team.changeFixtureDefFilter(CollisionCategoriesConstants.CATEGORY_TEAM1, CollisionCategoriesConstants.MASKBITS_TEAM1_THIN);
            else
                team.changeFixtureDefFilter(CollisionCategoriesConstants.CATEGORY_TEAM1, CollisionCategoriesConstants.MASKBITS_TEAM1_THICK);
            return;
        }
        if (isRemote)
            team.changeFixtureDefFilter(CollisionCategoriesConstants.CATEGORY_TEAM2, CollisionCategoriesConstants.MASKBITS_TEAM2_THIN);
        else
            team.changeFixtureDefFilter(CollisionCategoriesConstants.CATEGORY_TEAM2, CollisionCategoriesConstants.MASKBITS_TEAM2_THICK);
    }

    /** create new team depending on team control type which stored in extra */
    protected ITeam createTeam(String teamNameInExtra, IRace race) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));
        return new Team(teamNameInExtra, race, teamType);
    }

    /** init first team and planet */
    protected void initSecondPlanet(boolean isFakePlanet) {
        mSecondTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER
                        - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_RED_PLANET),
                GameStringsConstantsAndUtils.KEY_RED_PLANET,
                mSecondTeam
        ));
        mSecondTeam.getTeamPlanet().setSpawnPoint(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 -
                        SizeConstants.UNIT_SIZE - 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.GAME_FIELD_HEIGHT / 2
        );
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, long... unitUniqueId) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, getVertexBufferObjectManager(), team);
        planetStaticObject.setObjectDestroyedListener(new PlanetDestroyedListener(team));
        mStaticObjects.put(key, planetStaticObject);
        attachEntity(planetStaticObject);
        if (unitUniqueId.length > 0)
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        onEvent(new CreateCircleBodyEvent(planetStaticObject));
        return planetStaticObject;
    }

    /** init second team and planet */
    protected void initFirstPlanet(boolean isFakePlanet) {
        mFirstTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2
                        + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                mTextureRegionHolderUtils.getElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET),
                GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                mFirstTeam
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
        onEvent(new CreateCircleBodyEvent(sunStaticObject));
        return sunStaticObject;
    }

    /** move money text position on screen depending on planets position */
    private void initMoneyText() {
        LoggerHelper.methodInvocation(TAG, "initMoneyText");
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            if (!TeamControlBehaviourType.isUserControlType(team.getTeamControlType())) continue;
            LoggerHelper.methodInvocation(TAG, "init money text for " + team.getTeamName() + " team");
            /*
                Object, which display money value to user. Only one such money text present in the screen
                because one device can't be used by multiple users to play.
            */
            MoneyText moneyText = new MoneyText(team.getTeamName(),
                    getString(R.string.money_value_prefix), getVertexBufferObjectManager());
            mHud.attachChild(moneyText);
        }
    }

    protected void initBotControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledTeam");
        new Thread(new NormalBot(initializingTeam)).start();
    }

    /** init hud */
    private void initHud() {
        mHud = mCamera.getHUD();
        mHud.setTouchAreaBindingOnActionDownEnabled(true);
        mHud.setOnAreaTouchTraversalFrontToBack();
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

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final AbstractEntityEvent abstractEntityEvent) {
        final IAreaShape entity = abstractEntityEvent.getEntity();
        final Scene scene = abstractEntityEvent.hud() ? mCamera.getHUD() : mGameScene;
        if (abstractEntityEvent instanceof DetachEntityEvent) {
            DetachEntityEvent detachEntityEvent = (DetachEntityEvent) abstractEntityEvent;
            detachEntity(entity, scene,
                    entity instanceof RectangleWithBody && detachEntityEvent.withBody(),
                    detachEntityEvent.isUnregisterChildrenTouch());
        } else if (abstractEntityEvent instanceof AttachEntityEvent) {
            attachEntity(entity, scene, ((AttachEntityEvent) abstractEntityEvent).isRegisterChildrenTouch());
        }
    }

    /** detach entity from scene (hud or game scene) */
    private void detachEntity(final IAreaShape entity, final Scene scene,
                              final boolean withBody, final boolean unregisterChildrenTouch) {
        MainOperationsBaseGameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (withBody) {
                    ((RectangleWithBody) entity).removeBody(mPhysicsWorld);
                }
                if (unregisterChildrenTouch) {
                    LoggerHelper.printDebugMessage(TAG, "detach : entity.getChildCount()=" + entity.getChildCount());
                    for (int i = 0; i < entity.getChildCount(); i++) {
                        IEntity child = entity.getChildByIndex(i);
                        if (child instanceof ITouchArea) {
                            scene.unregisterTouchArea((ITouchArea) child);
                        }
                    }
                }
                scene.unregisterTouchArea(entity);
                scene.detachChild(entity);
                if (entity instanceof Unit)
                    mGameObjectsMap.remove(((Unit) entity).getObjectUniqueId());
            }
        });
    }

    /** attach entity to scene (hud or game scene) */
    private void attachEntity(final IAreaShape entity, final Scene scene, final boolean registerChildrenTouch) {
        MainOperationsBaseGameActivity.this.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (registerChildrenTouch) {
                    LoggerHelper.printDebugMessage(TAG, "attach : entity.getChildCount()=" + entity.getChildCount());
                    for (int i = 0; i < entity.getChildCount(); i++) {
                        IEntity child = entity.getChildByIndex(i);
                        if (child instanceof ITouchArea) {
                            scene.registerTouchArea((ITouchArea) child);
                        }
                    }
                }
                scene.attachChild(entity);
                scene.registerTouchArea(entity);
            }
        });
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateBuildingEvent createBuildingEvent) {
        userWantCreateBuilding(TeamsHolder.getInstance().getElement(createBuildingEvent.getTeamName()), createBuildingEvent.getBuildingId());
    }

    protected abstract void userWantCreateBuilding(ITeam userTeam, BuildingId buildingId);

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateUnitEvent abstractEntityEvent) {
        int unitKey = abstractEntityEvent.getKey();
        final ITeam team = TeamsHolder.getInstance().getElement(abstractEntityEvent.getTeamName());
        createUnit(unitKey, team, abstractEntityEvent.isTopPath());
    }

    /** create unit with body and update it's enemies and moving path */
    protected Unit createUnit(int unitKey, final ITeam unitTeam, boolean isTopPath) {
        float x = unitTeam.getTeamPlanet().getSpawnPointX(),
                y = unitTeam.getTeamPlanet().getSpawnPointY();
        Unit warrior = createThinUnit(unitKey, unitTeam, x, y);
        warrior.registerUpdateHandler();
        warrior.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        warrior.initMovingPath(UnitPathUtil.isLtrPath(x), isTopPath);
        return warrior;
    }

    /**
     * create unit (with physic body) in particular position and add it to team
     */
    protected Unit createThinUnit(int unitKey, ITeam unitTeam, float x, float y, long...
            unitUniqueId) {
        Unit unit = unitTeam.getTeamRace().getUnit(unitKey, unitTeam.getTeamColor());
        unit.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam));
        unit.setPosition(x, y);
        unitTeam.addObjectToTeam(unit);

        if (unitUniqueId.length > 0)
            unit.setObjectUniqueId(unitUniqueId[0]);
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);

        // init physic body
        BodyDef.BodyType bodyType = BodyDef.BodyType.DynamicBody;
        onEvent(new CreateCircleBodyEvent(unit, bodyType, unitTeam.getFixtureDefUnit()));
        if (unitTeam.getTeamControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE)
            unit.removeDamage();
        unit.setBulletFixtureDef(CollisionCategoriesConstants.getBulletFixtureDefByUnitCategory(
                unitTeam.getFixtureDefUnit().filter.categoryBits));

        attachEntity(unit);

        return unit;
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateCircleBodyEvent createCircleBodyEvent) {
        RectangleWithBody gameObject = createCircleBodyEvent.getGameObject();
        BodyDef.BodyType bodyType = createCircleBodyEvent.getBodyType();
        FixtureDef fixtureDef = createCircleBodyEvent.getFixtureDef();
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, gameObject, bodyType, fixtureDef);
        if (createCircleBodyEvent.isCustomBodyTransform()) {
            body.setTransform(createCircleBodyEvent.getX(), createCircleBodyEvent.getY(), createCircleBodyEvent.getAngle());
        }
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(gameObject, body, true, true));
        gameObject.setBody(body);
    }

    /** attach entity to game scene */
    private void attachEntity(final IAreaShape entity) {
        attachEntity(entity, mGameScene, false);
    }

    protected abstract void initThickClient();

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }
}