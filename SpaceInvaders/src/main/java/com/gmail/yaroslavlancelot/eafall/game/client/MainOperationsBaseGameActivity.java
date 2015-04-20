package com.gmail.yaroslavlancelot.eafall.game.client;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.ai.NormalBot;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.ContactListener;
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
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AbstractSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.DetachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.RunOnUpdateThreadEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.loading.GameResourceLoader;
import com.gmail.yaroslavlancelot.eafall.game.loading.GameResourcesLoaderFactory;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.scene.SceneManager;
import com.gmail.yaroslavlancelot.eafall.game.sound.MusicAndSoundsHandler;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.Team;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MoneyText;

import org.andengine.engine.camera.VelocityCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public abstract class MainOperationsBaseGameActivity extends BaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = MainOperationsBaseGameActivity.class.getCanonicalName();
    /** contains whole game units/warriors */
    private final Map<Long, GameObject> mGameObjectsMap = new HashMap<Long, GameObject>();
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
    private VelocityCamera mCamera;
    /** scene manager */
    private SceneManager mSceneManager;
    /** resource loader */
    private GameResourceLoader mGameResourceLoader;
    /** background theme */
    private MusicAndSoundsHandler mMusicAndSoundsHandler;
    private MusicAndSoundsHandler.BackgroundMusic mBackgroundMusic;

    /**
     * Parsing alliances from the intent and save to
     * {@link com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder}
     */
    private void createAlliances() {
        Intent intent = getIntent();
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.FIRST_TEAM_ALLIANCE),
                getVertexBufferObjectManager(), getMusicAndSoundsHandler());
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.SECOND_TEAM_ALLIANCE),
                getVertexBufferObjectManager(), getMusicAndSoundsHandler());
    }

    public MusicAndSoundsHandler getMusicAndSoundsHandler() {
        return mMusicAndSoundsHandler;
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
    public EngineOptions onCreateEngineOptions() {
        LoggerHelper.methodInvocation(TAG, "onCreateEngineOptions");
        // multi-touch
        if (!MultiTouch.isSupported(this)) {
            LoggerHelper.printErrorMessage(TAG, "MultiTouch isn't supported");
            finish();
        }
        //pre-in-game
        GameObject.clearCounter();
        // init camera
        mCamera = new VelocityCamera(0, 0,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                Config.getConfig().getMaxZoomFactor());
        mCamera.setBounds(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        mCamera.setBoundsEnabled(true);
        mCamera.setHUD(new HUD());
        //hud
        mHud = mCamera.getHUD();
        mHud.setTouchAreaBindingOnActionDownEnabled(true);
        mHud.setOnAreaTouchTraversalFrontToBack();

        EngineOptions engineOptions = new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT), mCamera
        );
        //physic world
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 2, 2);
        mPhysicsWorld.setContactListener(mContactListener = new ContactListener());
        //helpers
        mGameResourceLoader = new GameResourcesLoaderFactory().getLoader();
        mSceneManager = new SceneManager(this);
        // music
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);

        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) {
        LoggerHelper.methodInvocation(TAG, "onCreateResources");

        if (Config.getConfig().isProfilingEnabled()) {
            mGameResourceLoader.loadProfilingFonts(getTextureManager(), getFontManager());
        }
        mGameResourceLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());

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

    /** show profiling information on screen (using FPS logger) */
    private void profile() {
        final Text fpsText = new Text(200, SizeConstants.GAME_FIELD_HEIGHT - 50,
                FontHolder.getInstance().getElement("profiling"),
                "fps: 60.00", 20, getVertexBufferObjectManager());
        fpsText.setColor(Color.GREEN);
        mHud.attachChild(fpsText);
        mEngine.registerUpdateHandler(new FPSLogger(1) {
            @Override
            protected void onLogFPS() {
                fpsText.setText(String.format("fps: %.2f", this.mFrames / this.mSecondsElapsed));
            }
        });
    }

    protected void asyncGameLoading() {
        if (Config.getConfig().isProfilingEnabled()) {
            profile();
        }
        //game resources
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                EventBus.getDefault().register(MainOperationsBaseGameActivity.this);
                // music
                mMusicAndSoundsHandler = new MusicAndSoundsHandler(getSoundManager(), MainOperationsBaseGameActivity.this);
                mBackgroundMusic = mMusicAndSoundsHandler.new BackgroundMusic(getMusicManager());
                //alliance and player
                createAlliances();
                createTeams();
                //resources
                mGameResourceLoader.loadInGameImages(getTextureManager(), getVertexBufferObjectManager());
                mGameResourceLoader.loadInGameFonts(getTextureManager(), getFontManager());
                //scene
                Scene scene = mSceneManager.createGameScene(mCamera);
                scene.registerUpdateHandler(mPhysicsWorld);
                //attach SpriteGroups to the scene
                attachSpriteGroups(scene);
                //initSun
                createSun();
                //planets
                initFirstPlanet();
                initSecondPlanet();
                //other
                initMoneyText();
                initPopups();
                //pools
                BulletPool.init(getVertexBufferObjectManager());
                //sound
                mBackgroundMusic.initBackgroundMusic();
                mBackgroundMusic.playBackgroundMusic();
                //ready callback
                afterGameLoaded();
            }
        }));
    }

    private void attachSpriteGroups(Scene scene) {
        Object[] keys = SpriteGroupHolder.getsInstance().keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            scene.attachChild(SpriteGroupHolder.getGroup((String) key));
        }
    }

    public abstract void afterGameLoaded();

    public void replaceSplashSceneWithGameScene() {
        initThickClient();
        mSceneManager.replaceSplashSceneWithGame();
    }

    protected abstract void initThickClient();

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

    protected void createTeams() {
        //create
        Intent intent = getIntent();
        IAlliance race = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.FIRST_TEAM_ALLIANCE));
        mFirstTeam = createTeam(StringConstants.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, race);
        race = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.SECOND_TEAM_ALLIANCE));
        mSecondTeam = createTeam(StringConstants.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, race);
        //color
        mFirstTeam.setTeamColor(Color.BLUE);
        mSecondTeam.setTeamColor(Color.RED);
        //enemies
        mFirstTeam.setEnemyTeam(mSecondTeam);
        mSecondTeam.setEnemyTeam(mFirstTeam);
        //other
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
        if (team.getTeamName().equals(StringConstants.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE)) {
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

    /** init second team and planet */
    protected void initSecondPlanet() {
        PlanetStaticObject planet = createPlanet(
                SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_SECOND_PLANET),
                StringConstants.KEY_SECOND_PLANET,
                mSecondTeam);
        mSecondTeam.setTeamPlanet(planet);
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y,
                                              ITextureRegion textureRegion,
                                              String key,
                                              ITeam team,
                                              long... unitUniqueId) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion,
                getVertexBufferObjectManager());
        planetStaticObject.setTeam(team.getTeamName());
        planetStaticObject.initHealth(Config.getConfig().getPlanetHealth());
        planetStaticObject.addObjectDestroyedListener(new PlanetDestroyListener(team));
        attachSprite(planetStaticObject);
        if (unitUniqueId.length > 0) {
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        onEvent(new CreatePhysicBodyEvent(planetStaticObject));
        return planetStaticObject;
    }

    /** init first team and planet */
    protected void initFirstPlanet() {
        PlanetStaticObject planet = createPlanet(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_FIRST_PLANET),
                StringConstants.KEY_FIRST_PLANET, mFirstTeam);
        mFirstTeam.setTeamPlanet(planet);
    }

    /** create sun */
    protected SunStaticObject createSun() {
        ITextureRegion textureRegion = TextureRegionHolder.getRegion(StringConstants.KEY_SUN);

        SunStaticObject sunStaticObject = new SunStaticObject(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                textureRegion, mEngine.getVertexBufferObjectManager());
        attachSprite(sunStaticObject);
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

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final AbstractSpriteEvent abstractSpriteEvent) {
        final BatchedSprite sprite = abstractSpriteEvent.getSprite();
        Entity parent;
        //we have only 2 variants. Sprite was attached to the SpriteGroup or to the HUD.
        if (sprite.getSpriteGroupName() != null) {
            parent = SpriteGroupHolder.getGroup(sprite.getSpriteGroupName());
        } else {
            parent = mCamera.getHUD();
        }
        if (abstractSpriteEvent instanceof DetachSpriteEvent) {
            detachEntity(sprite, ((DetachSpriteEvent) abstractSpriteEvent).isBodied());
        } else if (abstractSpriteEvent instanceof AttachSpriteEvent) {
            attachSprite(sprite, parent);
        }
    }

    /** detach entity from entity (sprite group, hud or game scene) */
    private void detachEntity(final Sprite sprite, final boolean bodied) {
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (bodied) {
                    ((BodiedSprite) sprite).removeBody(mPhysicsWorld);
                }
                sprite.detachChildren();
                sprite.detachSelf();
                if (sprite instanceof Unit) {
                    mGameObjectsMap.remove(((Unit) sprite).getObjectUniqueId());
                }
            }
        });
    }

    /** attach sprite to entity (sprite group, hud or game scene) */
    private void attachSprite(final Sprite sprite, final Entity parent) {
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                parent.attachChild(sprite);
            }
        });
    }

    /** used by EventBus */
    @SuppressWarnings("unused")
    public void onEvent(final RunOnUpdateThreadEvent.UpdateThreadRunnable callback) {
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
                x - SizeConstants.UNIT_SIZE / 2,
                y - SizeConstants.UNIT_SIZE / 2);
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
        final Unit unit = unitTeam.getTeamRace().getUnitDummy(unitKey).constructUnit();
        if (unitUniqueId.length > 0) {
            unit.setObjectUniqueId(unitUniqueId[0]);
        }
        unit.init(x, y, unitTeam.getTeamName());
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);
        return unit;
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreatePhysicBodyEvent createPhysicBodyEvent) {
        BodiedSprite gameObject = createPhysicBodyEvent.getGameObject();
        BodyDef.BodyType bodyType = createPhysicBodyEvent.getBodyType();
        FixtureDef fixtureDef = createPhysicBodyEvent.getFixtureDef();
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, gameObject, bodyType, fixtureDef);
        if (createPhysicBodyEvent.isCustomBodyTransform()) {
            body.setTransform(createPhysicBodyEvent.getX(), createPhysicBodyEvent.getY(), createPhysicBodyEvent.getAngle());
        }
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(gameObject, body, true, false));
        gameObject.setBody(body);
    }

    /** attach entity to game scene */
    private void attachSprite(final BatchedSprite batchedSprite) {
        attachSprite(batchedSprite, SpriteGroupHolder.getGroup(batchedSprite.getSpriteGroupName()));
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
}