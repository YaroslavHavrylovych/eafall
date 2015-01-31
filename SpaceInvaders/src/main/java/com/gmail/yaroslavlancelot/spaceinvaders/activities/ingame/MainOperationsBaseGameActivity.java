package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import android.content.Intent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.ai.NormalBot;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.buildings.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.AbstractEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.AttachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.CreateCircleBodyEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.DetachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.units.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.units.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.callbacks.GameObjectsContactListener;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.callbacks.ObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.callbacks.PlanetDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupManager;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.ShowBuildingsPopupButtonSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.scene.SceneManager;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.CollisionCategoriesConstants;
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
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.controller.MultiTouch;
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
    /** game camera */
    private SmoothCamera mCamera;
    /** scene manager */
    private SceneManager mSceneManager;
    /** hold all texture regions used in current game */
    private TextureRegionHolderUtils mTextureRegionHolderUtils;
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

        mSceneManager = new SceneManager(this, mEngine);
        mSceneManager.loadSplashResources();

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

                Intent intent = getIntent();
                AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPathUtils.FIRST_TEAM_ALLIANCE),
                        getVertexBufferObjectManager(), getmMusicAndSoundsHandler());
                AllianceHolder.addAllianceByName(intent.getStringExtra(StringsAndPathUtils.SECOND_TEAM_ALLIANCE),
                        getVertexBufferObjectManager(), getmMusicAndSoundsHandler());

                mSceneManager.loadGameResources();
                mGameScene = mSceneManager.createGameScene(mCamera);
                mGameScene.registerUpdateHandler(mPhysicsWorld);
                initHud();
                initPlanetsAndTeams();

                mPhysicsWorld.setContactListener(mContactListener = new GameObjectsContactListener());
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
                ShowBuildingsPopupButtonSprite button = new ShowBuildingsPopupButtonSprite(getVertexBufferObjectManager());
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
                intent.getStringExtra(StringsAndPathUtils.FIRST_TEAM_ALLIANCE));
        mSecondTeam = createTeam(StringsAndPathUtils.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, race);
        race = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringsAndPathUtils.SECOND_TEAM_ALLIANCE));
        mFirstTeam = createTeam(StringsAndPathUtils.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, race);

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
        if (team.getTeamName().equals(StringsAndPathUtils.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE)) {
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
    protected ITeam createTeam(String teamNameInExtra, IAlliance race) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));
        return new Team(teamNameInExtra, race, teamType);
    }

    /** init first team and planet */
    protected void initSecondPlanet() {
        mSecondTeam.setTeamPlanet(createPlanet(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER
                        - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2,
                mTextureRegionHolderUtils.getElement(StringsAndPathUtils.KEY_RED_PLANET),
                StringsAndPathUtils.KEY_RED_PLANET,
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
        attachEntity(planetStaticObject);
        if (unitUniqueId.length > 0)
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        onEvent(new CreateCircleBodyEvent(planetStaticObject));
        return planetStaticObject;
    }

    /** init second team and planet */
    protected void initFirstPlanet() {
        mFirstTeam.setTeamPlanet(createPlanet(0, (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.PLANET_DIAMETER) / 2
                        + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                mTextureRegionHolderUtils.getElement(StringsAndPathUtils.KEY_BLUE_PLANET),
                StringsAndPathUtils.KEY_BLUE_PLANET,
                mFirstTeam
        ));
        mFirstTeam.getTeamPlanet().setSpawnPoint(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.UNIT_SIZE + 2,
                SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET);
    }

    /** create sun */
    protected SunStaticObject createSun() {
        float x = (SizeConstants.GAME_FIELD_WIDTH - SizeConstants.SUN_DIAMETER) / 2;
        float y = (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.SUN_DIAMETER) / 2;
        ITextureRegion textureRegion = mTextureRegionHolderUtils.getElement(StringsAndPathUtils.KEY_SUN);

        SunStaticObject sunStaticObject = new SunStaticObject(x, y, textureRegion, mEngine.getVertexBufferObjectManager());
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
        this.runOnUpdateThread(new Runnable() {
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

    /** attach entity to scene (hud or game scene)*/
    private void attachEntity(final IAreaShape entity, final Scene scene, final boolean registerChildrenTouch) {
        this.runOnUpdateThread(new Runnable() {
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
        movableUnit.initMovingPath(UnitPathUtil.isLtrPath(x), isTopPath);
        return movableUnit;
    }

    /** create unit */
    protected Unit createUnit(int unitKey, final ITeam unitTeam, float x, float y) {
        Unit unit = createThinUnit(unitKey, unitTeam,
                x - SizeConstants.UNIT_SIZE / 2,
                y - SizeConstants.UNIT_SIZE / 2);
        unit.registerUpdateHandler();
        unit.setEnemiesUpdater(UnitCallbacksUtils.getSimpleUnitEnemiesUpdater(unitTeam.getEnemyTeam()));
        return unit;
    }

    /**
     * create unit (with physic body) in particular position and add it to team.
     * Thin unit - unit without enemies update handler and behaviour update handler.
     */
    protected Unit createThinUnit(int unitKey, ITeam unitTeam, float x, float y, long...
            unitUniqueId) {
        Unit unit = unitTeam.getTeamRace().getUnit(unitKey, unitTeam.getTeamColor());
        unit.setObjectDestroyedListener(new ObjectDestroyedListener(unitTeam));
        unit.setPosition(x, y);
        unitTeam.addObjectToTeam(unit);

        if (unitUniqueId.length > 0) {
            unit.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);

        // init physic body
        onEvent(new CreateCircleBodyEvent(unit, unit.getBodyType(), unitTeam.getFixtureDefUnit()));
        if (unitTeam.getTeamControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE) {
            unit.removeDamage();
        }
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

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateStationaryUnitEvent unitEvent) {
        int unitKey = unitEvent.getKey();
        final ITeam team = TeamsHolder.getInstance().getElement(unitEvent.getTeamName());
        StationaryUnit unit = (StationaryUnit) createUnit(unitKey, team,
                unitEvent.getX(), unitEvent.getY());
    }

    protected abstract void initThickClient();

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }

    public MusicAndSoundsHandler getmMusicAndSoundsHandler() {
        return mMusicAndSoundsHandler;
    }
}