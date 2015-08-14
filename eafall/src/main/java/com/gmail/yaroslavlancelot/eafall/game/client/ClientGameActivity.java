package com.gmail.yaroslavlancelot.eafall.game.client;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.EaFallActivity;
import com.gmail.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.MissionIntent;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.SunStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.EnemiesFilterFactory;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.endgame.GameEndedEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AbstractSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.DetachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.Periodic;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.Player;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.IPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.information.GameOverPopup;
import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.ClientResourcesLoader;
import com.gmail.yaroslavlancelot.eafall.game.rule.IRuler;
import com.gmail.yaroslavlancelot.eafall.game.rule.RulesFactory;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Main game Activity. Extends {@link BaseGameActivity} class and contains main game elements.
 * Loads resources, initialize scene, engine and etc.
 */
public abstract class ClientGameActivity extends EaFallActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = ClientGameActivity.class.getCanonicalName();
    /** contains whole game units/warriors */
    private final Map<Long, GameObject> mGameObjectsMap = new HashMap<Long, GameObject>();
    /** first player */
    protected IPlayer mSecondPlayer;
    /** second player */
    protected IPlayer mFirstPlayer;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /** current mission/game config */
    protected MissionConfig mMissionConfig;
    /** game cycles (e.g. money increase, timer etc) */
    protected List<Periodic> mGamePeriodic = new ArrayList<Periodic>(2);
    /** defines whether the game is over and who is the winner */
    protected IRuler mRuler;

    @Override
    public EngineOptions onCreateEngineOptions() {
        EngineOptions engineOptions = super.onCreateEngineOptions();
        mMissionConfig = getIntent().getExtras().getParcelable(MissionIntent.MISSION_CONFIG);
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 2, 2);
        return engineOptions;
    }

    @Override
    protected void loadResources() {
        //game resources
        EventBus.getDefault().register(ClientGameActivity.this);
        //alliance and player
        createAlliances();
        createPlayers();
        //has to be before res loading as it sets units amount which used by res loader
        ((ClientResourcesLoader) mResourcesLoader).setMovableUnitsLimit(mMissionConfig.getMovableUnitsLimit());
        //resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
        //music
        if (EaFallApplication.getConfig().isMusicEnabled()) {
            mBackgroundMusic = new BackgroundMusic(
                    StringConstants.getMusicPath() + "background_1.ogg",
                    getMusicManager(), ClientGameActivity.this);
            mBackgroundMusic.initBackgroundMusic();
        }
        //whether or not the mission is bounded (timing)
        if (mMissionConfig.isTimerEnabled()) {
            mGamePeriodic.add(GameTime.getPeriodic(mMissionConfig.getTime()));
        }
        onResourcesLoaded();
    }

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        scene.setBackground(StringConstants.FILE_BACKGROUND, getVertexBufferObjectManager());
        mSceneManager.getWorkingScene().registerUpdateHandler(mPhysicsWorld);
        //attach SpriteGroups to the scene
        SpriteGroupHolder.attachSpriteGroups(scene, BatchingKeys.BatchTag.GAME_SCENE.value());
        //initSun
        createSun();
        //planets
        initFirstPlanet();
        initSecondPlanet();
        //hud
        mHud.initHudElements(mCamera, getVertexBufferObjectManager(), mMissionConfig);
        //pools
        BulletPool.init(getVertexBufferObjectManager());
        //sound
        if (EaFallApplication.getConfig().isSoundsEnabled()) {
            SoundFactory.getInstance().setCameraHandler(
                    mSceneManager.getWorkingScene().getCameraHandler());
        }
    }

    protected void hideSplash() {
        initThickClient();
        super.hideSplash();
    }

    @Override
    protected void onShowWorkingScene() {
        startRuler();
        startPeriodic();
    }

    /** start tracker which tracks game rules */
    protected void startRuler() {
        mRuler = RulesFactory.createRuler(
                mMissionConfig.getMissionType(),
                mMissionConfig.getValue(),
                mMissionConfig.isTimerEnabled());
        mRuler.startTracking();
    }

    /**
     * registers all update handlers from #mGamePeriodic
     * <br/>
     * e.g. money update events, game time ticking events
     */
    protected void startPeriodic() {
        for (Periodic periodic : mGamePeriodic) {
            mEngine.registerUpdateHandler(periodic.getUpdateHandler());
        }
    }

    protected abstract void initThickClient();

    protected void createPlayers() {
        //create
        Intent intent = getIntent();
        IAlliance alliance = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.FIRST_PLAYER_ALLIANCE));
        mFirstPlayer = createPlayer(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE, alliance);
        alliance = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.SECOND_PLAYER_ALLIANCE));
        mSecondPlayer = createPlayer(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE, alliance);
        //color
        mFirstPlayer.setColor(Color.BLUE);
        mSecondPlayer.setColor(Color.RED);
        //enemies
        mFirstPlayer.setEnemyPlayer(mSecondPlayer);
        mSecondPlayer.setEnemyPlayer(mFirstPlayer);
        //other
        initPlayer(mFirstPlayer);
        initPlayer(mSecondPlayer);
    }

    /**
     * initialize player (init user or bot player, or do nothing if player control from remote)
     *
     * @param player player to init
     */
    private void initPlayer(IPlayer player) {
        IPlayer.ControlType playerType = player.getControlType();
        initPlayerFixtureDef(player);

        if (playerType == IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE) {
            initBotControlledPlayer(player);
        }

        PlayersHolder.getInstance().addElement(player.getName(), player);
    }

    protected void initPlayerFixtureDef(IPlayer player) {
        boolean isRemote = player.getControlType().isClientSide();
        if (player.getName().equals(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE)) {
            if (isRemote)
                player.changeFixtureDefFilter(CollisionCategories.CATEGORY_PLAYER1, CollisionCategories.MASKBITS_PLAYER1_THIN);
            else
                player.changeFixtureDefFilter(CollisionCategories.CATEGORY_PLAYER1, CollisionCategories.MASKBITS_PLAYER1_THICK);
            return;
        }
        if (isRemote)
            player.changeFixtureDefFilter(CollisionCategories.CATEGORY_PLAYER2, CollisionCategories.MASKBITS_PLAYER2_THIN);
        else
            player.changeFixtureDefFilter(CollisionCategories.CATEGORY_PLAYER2, CollisionCategories.MASKBITS_PLAYER2_THICK);
    }

    /** create new player depending on player control type which stored in extra */
    protected IPlayer createPlayer(String playerNameInExtra, IAlliance alliance) {
        Intent intent = getIntent();
        IPlayer.ControlType playerType = IPlayer.ControlType.valueOf(intent.getStringExtra(playerNameInExtra));
        return new Player(playerNameInExtra, alliance, playerType, mMissionConfig);
    }

    /** init second player and planet */
    protected void initSecondPlanet() {
        PlanetStaticObject planet = createPlanet(
                SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_SECOND_PLANET),
                StringConstants.KEY_SECOND_PLANET,
                mSecondPlayer);
        mSecondPlayer.setPlanet(planet);
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y,
                                              ITextureRegion textureRegion,
                                              String key,
                                              IPlayer player,
                                              long... unitUniqueId) {
        LoggerHelper.methodInvocation(TAG, "createPlanet");
        PlanetStaticObject planet = new PlanetStaticObject(x, y, textureRegion,
                getVertexBufferObjectManager());
        planet.init(player.getName(), mMissionConfig.getPlanetHealth());
        planet.addObjectDestroyedListener(new PlanetDestroyListener(player));
        attachSprite(planet);
        if (unitUniqueId.length > 0) {
            planet.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(planet.getObjectUniqueId(), planet);
        onEvent(new CreatePhysicBodyEvent(planet));
        return planet;
    }

    /** init first player and planet */
    protected void initFirstPlanet() {
        PlanetStaticObject planet = createPlanet(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_FIRST_PLANET),
                StringConstants.KEY_FIRST_PLANET, mFirstPlayer);
        mFirstPlayer.setPlanet(planet);
    }

    /** create sun */
    protected SunStaticObject createSun() {
        SunStaticObject sunStaticObject = new SunStaticObject(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                mEngine.getVertexBufferObjectManager());
        attachSprite(sunStaticObject);
        mGameObjectsMap.put(sunStaticObject.getObjectUniqueId(), sunStaticObject);
        onEvent(new CreatePhysicBodyEvent(sunStaticObject));
        return sunStaticObject;
    }

    protected void initBotControlledPlayer(final IPlayer initializingPlayer) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledPlayer");
        new Thread(new VeryFirstBot(initializingPlayer)).start();
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

    /**
     * Parsing alliances from the intent and save to
     * {@link com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder}
     */
    private void createAlliances() {
        Intent intent = getIntent();
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.FIRST_PLAYER_ALLIANCE),
                getVertexBufferObjectManager());
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.SECOND_PLAYER_ALLIANCE),
                getVertexBufferObjectManager());
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateBuildingEvent createBuildingEvent) {
        userWantCreateBuilding(PlayersHolder.getInstance().getElement(createBuildingEvent.getPlayerName()), createBuildingEvent.getBuildingId());
    }

    protected abstract void userWantCreateBuilding(IPlayer userPlayer, BuildingId buildingId);

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateMovableUnitEvent unitEvent) {
        final IPlayer player = PlayersHolder.getInstance().getElement(unitEvent.getPlayerName());
        //check units amount limit
        if (player.getUnitsAmount() >= mMissionConfig.getMovableUnitsLimit()) {
            return;
        }
        int unitKey = unitEvent.getKey();
        createMovableUnit(player, unitKey, unitEvent.getX(), unitEvent.getY(), unitEvent.isTopPath());
    }

    public MovableUnit createMovableUnit(IPlayer unitPlayer,
                                         int unitKey, int x, int y, boolean isTopPath) {
        MovableUnit movableUnit = (MovableUnit) createUnit(unitKey, unitPlayer, x, y);
        movableUnit.initMovingPath(PathHelper.isLtrPath(x), isTopPath);
        return movableUnit;
    }

    /** create unit */
    protected Unit createUnit(int unitKey, final IPlayer unitPlayer, float x, float y) {
        Unit unit = createThinUnit(unitKey, unitPlayer, x, y);
        unit.setEnemiesUpdater(EnemiesFilterFactory.getFilter(unitPlayer.getEnemyPlayer()));
        unit.registerUpdateHandler();
        return unit;
    }

    /**
     * create unit (with physic body) in particular position and add it to player.
     * Thin unit - unit without enemies update handler and behaviour update handler.
     */
    protected Unit createThinUnit(int unitKey, final IPlayer unitPlayer, float x, float y, long...
            unitUniqueId) {
        final Unit unit = unitPlayer.constructUnit(unitKey);
        if (unitUniqueId.length > 0) {
            unit.setObjectUniqueId(unitUniqueId[0]);
        }
        unit.init(x, y);
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
            body.setTransform(createPhysicBodyEvent.getX(),
                    createPhysicBodyEvent.getY(), createPhysicBodyEvent.getAngle());
        }
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(gameObject, body, true, false));
        gameObject.setBody(body);
    }

    /** attach entity to game scene */
    private void attachSprite(final BatchedSprite batchedSprite) {
        attachSprite(batchedSprite, SpriteGroupHolder.getGroup(batchedSprite.getSpriteGroupName()));
    }

    @SuppressWarnings("unused")
    public void onEvent(final GameEndedEvent gameEndedEvent) {
        IPopup popup = PopupManager.getPopup(GameOverPopup.KEY);
        ((GameOverPopup) popup).setSuccess(gameEndedEvent.isSuccess());
        popup.setStateChangingListener(new IPopup.StateChangingListener() {
            @Override
            public void onShowed() {
            }

            @Override
            public void onHided() {
                ClientGameActivity.this.finish();
            }
        });
        popup.showPopup();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateStationaryUnitEvent unitEvent) {
        int unitKey = unitEvent.getKey();
        final IPlayer player = PlayersHolder.getInstance().getElement(unitEvent.getPlayerName());
        StationaryUnit unit = (StationaryUnit) createUnit(unitKey, player,
                unitEvent.getX(), unitEvent.getY());
    }

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }
}