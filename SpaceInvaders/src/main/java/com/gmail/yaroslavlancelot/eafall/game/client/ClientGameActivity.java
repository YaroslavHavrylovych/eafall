package com.gmail.yaroslavlancelot.eafall.game.client;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.GameActivity;
import com.gmail.yaroslavlancelot.eafall.game.SharedDataCallbacks;
import com.gmail.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.SunStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.EnemiesFilterFactory;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AbstractSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.DetachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.Player;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.other.HealthBarCarcassSprite;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MoneyText;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MovableUnitsLimitText;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
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
public abstract class ClientGameActivity extends GameActivity {
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

    @Override
    public EngineOptions onCreateEngineOptions() {
        EngineOptions engineOptions = super.onCreateEngineOptions();
        //physic world
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 2, 2);
        return engineOptions;
    }

    @Override
    protected void asyncResourcesLoading() {
        //game resources
        EventBus.getDefault().register(ClientGameActivity.this);
        //alliance and player
        createAlliances();
        createPlayers();
        //resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
        //scene
        EaFallScene scene = mSceneManager.getWorkingScene();
        scene.setBackground(StringConstants.FILE_BACKGROUND, getVertexBufferObjectManager());
        mSceneManager.getWorkingScene().registerUpdateHandler(mPhysicsWorld);
        //attach SpriteGroups to the scene
        attachSpriteGroups(scene, BatchingKeys.BatchTag.GAME_SCENE.value());
        //initSun
        createSun();
        //planets
        initFirstPlanet();
        initSecondPlanet();
        //other
        initHudElements();
        initPopups();
        //pools
        BulletPool.init(getVertexBufferObjectManager());
        //sound
        if (Config.getConfig().isSoundsEnabled()) {
            SoundFactory.getInstance().setCameraHandler(
                    mSceneManager.getWorkingScene().getCameraHandler());
        }
        //music
        if (Config.getConfig().isMusicEnabled()) {
            mBackgroundMusic = new BackgroundMusic(
                    StringConstants.getMusicPath() + "background_1.ogg",
                    getMusicManager(), ClientGameActivity.this);
            mBackgroundMusic.initBackgroundMusic();
            mBackgroundMusic.playBackgroundMusic();
        }
        onResourcesLoaded();
    }

    public void hideSplash() {
        initThickClient();
        super.hideSplash();
    }

    protected abstract void initThickClient();

    /**
     * attache {@link SpriteGroup} returned with {@link SpriteGroupHolder#getElements()}
     * to the scene if SpriteGroup has tag which equal to groupKey
     *
     * @param scene    to attach SpriteGroups
     * @param groupKey SpriteGroup tag
     */
    private void attachSpriteGroups(Scene scene, int groupKey) {
        Object[] keys = SpriteGroupHolder.getsInstance().keySet().toArray();
        Arrays.sort(keys);
        SpriteGroup spriteGroup;
        for (Object key : keys) {
            spriteGroup = SpriteGroupHolder.getGroup((String) key);
            if (spriteGroup.getTag() == groupKey) {
                scene.attachChild(spriteGroup);
            }
        }
    }

    private void initPopups() {
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            if (player.getControlType() == IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE ||
                    player.getControlType() == IPlayer.ControlType.USER_CONTROL_ON_CLIENT_SIDE) {
                PopupManager.init(player.getName(), mHud, mCamera, getVertexBufferObjectManager());
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
        IPlayer.ControlType type = player.getControlType();
        boolean isRemote = IPlayer.ControlType.isClientSide(type);
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
        return new Player(playerNameInExtra, alliance, playerType);
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
        PlanetStaticObject planetStaticObject = new PlanetStaticObject(x, y, textureRegion,
                getVertexBufferObjectManager());
        planetStaticObject.setPlayer(player.getName());
        planetStaticObject.initHealth(Config.getConfig().getPlanetHealth());
        planetStaticObject.addObjectDestroyedListener(new PlanetDestroyListener(player));
        attachSprite(planetStaticObject);
        if (unitUniqueId.length > 0) {
            planetStaticObject.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(planetStaticObject.getObjectUniqueId(), planetStaticObject);
        onEvent(new CreatePhysicBodyEvent(planetStaticObject));
        return planetStaticObject;
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

    /** init all HUD stuff */
    private void initHudElements() {
        LoggerHelper.methodInvocation(TAG, "initHudElements");
        attachSpriteGroups(mHud, BatchingKeys.BatchTag.GAME_HUD.value());
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            if (!IPlayer.ControlType.isUserControlType(player.getControlType())) continue;
            LoggerHelper.methodInvocation(TAG, "init money text for " + player.getName() + " player");
            /*
                Object, which display money value to user. Only one such money text present in the screen
                because one device can't be used by multiple users to play.
            */
            VertexBufferObjectManager objectManager = getVertexBufferObjectManager();
            //money
            MoneyText moneyText = new MoneyText(player.getName(),
                    getString(R.string.money_value_prefix), objectManager);
            mHud.attachChild(moneyText);
            //menu
            MenuPopupButton menuButton = new MenuPopupButton(objectManager);
            mHud.attachChild(menuButton);
            //health carcass
            HealthBarCarcassSprite healthBarCarcassSprite = new HealthBarCarcassSprite(objectManager);
            SpriteGroupHolder.getGroup(BatchingKeys.PLAYER_HEALTH).attachChild(healthBarCarcassSprite);
            //TODO take a look on this text, I don't it's positioned like that (not place but code)
            final MovableUnitsLimitText limitText = new MovableUnitsLimitText(
                    moneyText.getX(), SizeConstants.GAME_FIELD_HEIGHT - 130,
                    getVertexBufferObjectManager());
            mHud.attachChild(limitText);
            final String key = ((Player) player).MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY;
            SharedDataCallbacks.addCallback(new SharedDataCallbacks.DataChangedCallback(key) {
                @Override
                public void callback(String callbackKey, Object value) {
                    if (key.equals(callbackKey)) {
                        limitText.setValue((Integer) value);
                    }
                }
            });
        }
    }

    protected void initBotControlledPlayer(final IPlayer initializingPlayer) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledPlayer");
        new Thread(new VeryFirstBot(initializingPlayer, ClientGameActivity.this)).start();
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
    public synchronized void onEvent(final CreateMovableUnitEvent unitEvent) {
        final IPlayer player = PlayersHolder.getInstance().getElement(unitEvent.getPlayerName());
        //check units amount limit
        if (player.getUnitsAmount() >= Config.getConfig().getMovableUnitsLimit()) {
            return;
        }
        int unitKey = unitEvent.getKey();
        createMovableUnit(unitKey, player, unitEvent.isTopPath());
    }

    /** create unit with body and update it's enemies and moving path */
    protected MovableUnit createMovableUnit(int unitKey, final IPlayer unitPlayer, boolean isTopPath) {
        float x = unitPlayer.getPlanet().getSpawnPointX(),
                y = unitPlayer.getPlanet().getSpawnPointY();
        MovableUnit movableUnit = (MovableUnit) createUnit(unitKey, unitPlayer, x, y);
        movableUnit.initMovingPath(StaticHelper.isLtrPath(x), isTopPath);
        return movableUnit;
    }

    /** create unit */
    protected Unit createUnit(int unitKey, final IPlayer unitPlayer, float x, float y) {
        Unit unit = createThinUnit(unitKey, unitPlayer,
                x - SizeConstants.UNIT_SIZE / 2,
                y - SizeConstants.UNIT_SIZE / 2);
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