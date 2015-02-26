package com.gmail.yaroslavlancelot.eafall.game.client;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.BuildConfig;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.ai.NormalBot;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.ContactListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.RectangleWithBody;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.SunStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.EnemiesFilter;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AbstractEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.DetachEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.RunOnUpdateThreadEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.scene.SceneManager;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.GameScene;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;
import com.gmail.yaroslavlancelot.eafall.game.sound.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.Team;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MoneyText;

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
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.time.TimeConstants;

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
    protected ContactListener mContactListener;
    /** game camera */
    private SmoothCamera mCamera;
    /** scene manager */
    private SceneManager mSceneManager;
    /** hold all texture regions used in current game */
    private TextureRegionHolder mTextureRegionHolderUtils;
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
        mCamera = new SmoothCamera(0, 0, Sizes.GAME_FIELD_WIDTH, Sizes.GAME_FIELD_HEIGHT,
                Sizes.GAME_FIELD_WIDTH, Sizes.GAME_FIELD_HEIGHT, 1.0f);
        mCamera.setBounds(0, 0, Sizes.GAME_FIELD_WIDTH, Sizes.GAME_FIELD_HEIGHT);
        mCamera.setBoundsEnabled(true);
        mCamera.setHUD(new HUD());

        EngineOptions engineOptions = new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
                Sizes.GAME_FIELD_WIDTH, Sizes.GAME_FIELD_HEIGHT), mCamera
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

        if (BuildConfig.DEBUG) {
            mEngine.registerUpdateHandler(new FPSLogger(1) {
                @Override
                protected void onLogFPS() {
                    LoggerHelper.printVerboseMessage(TAG, String.format("FPS: %.2f (MIN: %.0f ms | MAX: %.0f ms)",
                            this.mFrames / this.mSecondsElapsed,
                            this.mShortestFrame * TimeConstants.MILLISECONDS_PER_SECOND,
                            this.mLongestFrame * TimeConstants.MILLISECONDS_PER_SECOND));
                }
            });
        }

        mTextureRegionHolderUtils = TextureRegionHolder.getInstance();

        mSceneManager = new SceneManager(this);
        SplashScene.loadResources(this, getTextureManager());

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback onCreateSceneCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateScene");
        mSceneManager.createSplashScene();
        onCreateSceneCallback.onCreateSceneFinished(mSceneManager.getSplashScene());
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        onPopulateSceneCallback.onPopulateSceneFinished();

        asyncGameLoading();
    }

    protected void asyncGameLoading() {
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                // music
                mMusicAndSoundsHandler = new MusicAndSoundsHandler(getSoundManager(), MainOperationsBaseGameActivity.this);
                mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(getMusicManager());

                BulletPool.init(getVertexBufferObjectManager());

                Intent intent = getIntent();
                AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPath.FIRST_TEAM_ALLIANCE),
                        getVertexBufferObjectManager(), getmMusicAndSoundsHandler());
                AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPath.SECOND_TEAM_ALLIANCE),
                        getVertexBufferObjectManager(), getmMusicAndSoundsHandler());
                for (IAlliance race : AllianceHolder.getInstance().getElements()) {
                    race.loadResources(getTextureManager());
                }

                PopupManager.loadResource(MainOperationsBaseGameActivity.this, getTextureManager());
                GameScene.loadResources(MainOperationsBaseGameActivity.this, getTextureManager());

                // font
                FontHolder.loadGeneralGameFonts(getFontManager(), getTextureManager());
                DescriptionPopupHud.loadFonts(getFontManager(), getTextureManager());
                mGameScene = mSceneManager.createGameScene(mCamera);
                mGameScene.registerUpdateHandler(mPhysicsWorld);
                initHud();
                initPlanetsAndTeams();

                mPhysicsWorld.setContactListener(mContactListener = new ContactListener());
                mBackgroundMusic.initBackgroundMusic();
                mBackgroundMusic.playBackgroundMusic();

                afterGameLoaded();
            }
        }));
    }

    public abstract void afterGameLoaded();

    public void replaceSplashSceneWithGameScene() {
        EventBus.getDefault().register(this);
        initThickClient();
        mSceneManager.replaceSplashSceneWithGame();
    }

    protected abstract void initThickClient();

    protected void initPlanetsAndTeams() {
        //initSun
        createSun();

        initTeams();
        // first team init
        initFirstPlanet();
        // second team init
        initSecondPlanet();

        initMoneyText();
        initPopups();
    }

    private void initPopups() {
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            if (team.getTeamControlType() == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE ||
                    team.getTeamControlType() == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE) {
                PopupManager.init(team.getTeamName(), mHud, mCamera, getVertexBufferObjectManager());
                //buildings
                ConstructionPopupButton button = new ConstructionPopupButton(getVertexBufferObjectManager());
                button.setOnClickListener(new ButtonSprite.OnClickListener() {
                    @Override
                    public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                        PopupManager.getPopup(BuildingsPopupHud.KEY).triggerPopup();
                    }
                });
                mHud.attachChild(button);
                mHud.registerTouchArea(button);
            }
        }
    }

    protected void initTeams() {
        // red team

        Intent intent = getIntent();
        IAlliance race = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringsAndPath.FIRST_TEAM_ALLIANCE));
        mSecondTeam = createTeam(StringsAndPath.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, race);
        race = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringsAndPath.SECOND_TEAM_ALLIANCE));
        mFirstTeam = createTeam(StringsAndPath.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, race);

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
        if (team.getTeamName().equals(StringsAndPath.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE)) {
            if (isRemote)
                team.changeFixtureDefFilter(CollisionCategories.CATEGORY_TEAM1, CollisionCategories.MASKBITS_TEAM1_THIN);
            else
                team.changeFixtureDefFilter(CollisionCategories.CATEGORY_TEAM1, CollisionCategories.MASKBITS_TEAM1_THICK);
            return;
        }
        if (isRemote)
            team.changeFixtureDefFilter(CollisionCategories.CATEGORY_TEAM2, CollisionCategories.MASKBITS_TEAM2_THIN);
        else
            team.changeFixtureDefFilter(CollisionCategories.CATEGORY_TEAM2, CollisionCategories.MASKBITS_TEAM2_THICK);
    }

    /** create new team depending on team control type which stored in extra */
    protected ITeam createTeam(String teamNameInExtra, IAlliance race) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));
        return new Team(teamNameInExtra, race, teamType);
    }

    /** init first team and planet */
    protected void initSecondPlanet() {
        PlanetStaticObject planet = createPlanet(
                Sizes.GAME_FIELD_WIDTH - Sizes.PLANET_DIAMETER / 2 - Sizes.ADDITION_MARGIN_FOR_PLANET,
                Sizes.HALF_FIELD_HEIGHT,
                mTextureRegionHolderUtils.getElement(StringsAndPath.KEY_RED_PLANET),
                StringsAndPath.KEY_RED_PLANET,
                mSecondTeam);
        mSecondTeam.setTeamPlanet(planet);
        mSecondTeam.getTeamPlanet().setSpawnPoint(
                planet.getX() - Sizes.PLANET_DIAMETER / 2 - Sizes.UNIT_SIZE - Sizes.ADDITION_MARGIN_FOR_PLANET,
                planet.getY());
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y, ITextureRegion textureRegion, String key, ITeam team, long... unitUniqueId) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion, getVertexBufferObjectManager(), team);
        planetStaticObject.addObjectDestroyedListener(new PlanetDestroyListener(team));
        attachEntity(planetStaticObject);
        if (unitUniqueId.length > 0) {
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        onEvent(new CreatePhysicBodyEvent(planetStaticObject));
        return planetStaticObject;
    }

    /** init second team and planet */
    protected void initFirstPlanet() {
        PlanetStaticObject planet = createPlanet(Sizes.PLANET_DIAMETER / 2 + Sizes.ADDITION_MARGIN_FOR_PLANET,
                Sizes.HALF_FIELD_HEIGHT,
                mTextureRegionHolderUtils.getElement(StringsAndPath.KEY_BLUE_PLANET),
                StringsAndPath.KEY_BLUE_PLANET, mFirstTeam);
        mFirstTeam.setTeamPlanet(planet);
        mFirstTeam.getTeamPlanet().setSpawnPoint(
                Sizes.PLANET_DIAMETER + Sizes.ADDITION_MARGIN_FOR_PLANET + Sizes.UNIT_SIZE,
                planet.getY());
    }

    /** create sun */
    protected SunStaticObject createSun() {
        float x = Sizes.HALF_FIELD_WIDTH;
        float y = Sizes.HALF_FIELD_HEIGHT;
        ITextureRegion textureRegion = mTextureRegionHolderUtils.getElement(StringsAndPath.KEY_SUN);

        SunStaticObject sunStaticObject = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
        attachEntity(sunStaticObject);
        mGameObjectsMap.put(sunStaticObject.getObjectUniqueId(), sunStaticObject);
        onEvent(new CreatePhysicBodyEvent(sunStaticObject));
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
        final Shape entity = abstractEntityEvent.getEntity();
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
    private void detachEntity(final Shape entity, final Scene scene,
                              final boolean withBody, final boolean unregisterChildrenTouch) {
        runOnUpdateThread(new Runnable() {
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
                if (entity instanceof Unit) {
                    mGameObjectsMap.remove(((Unit) entity).getObjectUniqueId());
                }
            }
        });
    }

    /** attach entity to scene (hud or game scene) */
    private void attachEntity(final Shape entity, final Scene scene, final boolean registerChildrenTouch) {
        runOnUpdateThread(new Runnable() {
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
                scene.registerTouchArea(entity);
                scene.attachChild(entity);
            }
        });
    }

    /** used by EventBus */
    @SuppressWarnings("unused")
    public void onEvent(final RunOnUpdateThreadEvent.UpdateThreadRunnable callback) {
        //TODO move this to thread pool
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                callback.updateThreadCallback();
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
    public void onEvent(final CreateMovableUnitEvent unitEvent) {
        int unitKey = unitEvent.getKey();
        final ITeam team = TeamsHolder.getInstance().getElement(unitEvent.getTeamName());
        createMovableUnit(unitKey, team, unitEvent.isTopPath());
    }

    /** create unit with body and update it's enemies and moving path */
    protected MovableUnit createMovableUnit(int unitKey, final ITeam unitTeam, boolean isTopPath) {
        float x = unitTeam.getTeamPlanet().getSpawnPointX(),
                y = unitTeam.getTeamPlanet().getSpawnPointY();
        MovableUnit movableUnit = (MovableUnit) createUnit(unitKey, unitTeam, x, y);
        movableUnit.initMovingPath(StaticHelper.isLtrPath(x), isTopPath);
        return movableUnit;
    }

    /** create unit */
    protected Unit createUnit(int unitKey, final ITeam unitTeam, float x, float y) {
        Unit unit = createThinUnit(unitKey, unitTeam,
                x - Sizes.UNIT_SIZE / 2,
                y - Sizes.UNIT_SIZE / 2);
        unit.registerUpdateHandler();
        unit.setEnemiesUpdater(EnemiesFilter.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        return unit;
    }

    /**
     * create unit (with physic body) in particular position and add it to team.
     * Thin unit - unit without enemies update handler and behaviour update handler.
     */
    protected Unit createThinUnit(int unitKey, final ITeam unitTeam, float x, float y, long...
            unitUniqueId) {
        final Unit unit = unitTeam.getTeamRace().getUnit(unitKey, unitTeam.getTeamColor());
        if (unitUniqueId.length > 0) {
            unit.setObjectUniqueId(unitUniqueId[0]);
        }

        unit.init(unitTeam.getTeamName(), x, y);
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);
        return unit;
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreatePhysicBodyEvent createPhysicBodyEvent) {
        RectangleWithBody gameObject = createPhysicBodyEvent.getGameObject();
        BodyDef.BodyType bodyType = createPhysicBodyEvent.getBodyType();
        FixtureDef fixtureDef = createPhysicBodyEvent.getFixtureDef();
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, gameObject, bodyType, fixtureDef);
        if (createPhysicBodyEvent.isCustomBodyTransform()) {
            body.setTransform(createPhysicBodyEvent.getX(), createPhysicBodyEvent.getY(), createPhysicBodyEvent.getAngle());
        }
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(gameObject, body, true, true));
        gameObject.setBody(body);
    }

    /** attach entity to game scene */
    private void attachEntity(final Shape entity) {
        attachEntity(entity, mGameScene, false);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateStationaryUnitEvent unitEvent) {
        int unitKey = unitEvent.getKey();
        final ITeam team = TeamsHolder.getInstance().getElement(unitEvent.getTeamName());
        StationaryUnit unit = (StationaryUnit) createUnit(unitKey, team,
                unitEvent.getX(), unitEvent.getY());
    }

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }

    public MusicAndSoundsHandler getmMusicAndSoundsHandler() {
        return mMusicAndSoundsHandler;
    }
}