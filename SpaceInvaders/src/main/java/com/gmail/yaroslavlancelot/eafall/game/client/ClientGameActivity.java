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
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.Team;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MoneyText;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MovableUnitsLimitText;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
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
public abstract class ClientGameActivity extends GameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = ClientGameActivity.class.getCanonicalName();
    /** contains whole game units/warriors */
    private final Map<Long, GameObject> mGameObjectsMap = new HashMap<Long, GameObject>();
    /** first team */
    protected ITeam mSecondTeam;
    /** second team */
    protected ITeam mFirstTeam;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /** game objects contact listener */
    protected ContactListener mContactListener;

    @Override
    public EngineOptions onCreateEngineOptions() {
        EngineOptions engineOptions = super.onCreateEngineOptions();
        //physic world
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 2, 2);
        mPhysicsWorld.setContactListener(mContactListener = new ContactListener());
        return engineOptions;
    }

    @Override
    protected void asyncResourcesLoading() {
        //game resources
        EventBus.getDefault().register(ClientGameActivity.this);
        //alliance and player
        createAlliances();
        createTeams();
        //resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
        //scene
        EaFallScene scene = mSceneManager.getWorkingScene();
        scene.setBackground(StringConstants.FILE_BACKGROUND, getVertexBufferObjectManager());
        mSceneManager.getWorkingScene().registerUpdateHandler(mPhysicsWorld);
        //attach SpriteGroups to the scene
        attachSpriteGroups(scene);
        //initSun
        createSun();
        //planets
        initFirstPlanet();
        initSecondPlanet();
        //other
        initOnScreenText();
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

    private void attachSpriteGroups(Scene scene) {
        Object[] keys = SpriteGroupHolder.getsInstance().keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            scene.attachChild(SpriteGroupHolder.getGroup((String) key));
        }
    }

    private void initPopups() {
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            if (team.getControlType() == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE ||
                    team.getControlType() == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE) {
                PopupManager.init(team.getName(), mHud, mCamera, getVertexBufferObjectManager());
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
        IAlliance alliance = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.FIRST_TEAM_ALLIANCE));
        mFirstTeam = createTeam(StringConstants.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, alliance);
        alliance = AllianceHolder.getInstance().getElement(
                intent.getStringExtra(StringConstants.SECOND_TEAM_ALLIANCE));
        mSecondTeam = createTeam(StringConstants.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, alliance);
        //color
        mFirstTeam.setColor(Color.BLUE);
        mSecondTeam.setColor(Color.RED);
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
        TeamControlBehaviourType teamType = team.getControlType();
        initTeamFixtureDef(team);

        if (teamType == TeamControlBehaviourType.BOT_CONTROL_ON_SERVER_SIDE) {
            initBotControlledTeam(team);
        }

        TeamsHolder.getInstance().addElement(team.getName(), team);
    }

    protected void initTeamFixtureDef(ITeam team) {
        TeamControlBehaviourType type = team.getControlType();
        boolean isRemote = TeamControlBehaviourType.isClientSide(type);
        if (team.getName().equals(StringConstants.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE)) {
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
    protected ITeam createTeam(String teamNameInExtra, IAlliance alliance) {
        Intent intent = getIntent();
        TeamControlBehaviourType teamType = TeamControlBehaviourType.valueOf(intent.getStringExtra(teamNameInExtra));
        return new Team(teamNameInExtra, alliance, teamType);
    }

    /** init second team and planet */
    protected void initSecondPlanet() {
        PlanetStaticObject planet = createPlanet(
                SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_SECOND_PLANET),
                StringConstants.KEY_SECOND_PLANET,
                mSecondTeam);
        mSecondTeam.setPlanet(planet);
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
        planetStaticObject.setTeam(team.getName());
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
        mFirstTeam.setPlanet(planet);
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

    /** money text initialization */
    private void initOnScreenText() {
        LoggerHelper.methodInvocation(TAG, "initOnScreenText");
        for (final ITeam team : TeamsHolder.getInstance().getElements()) {
            if (!TeamControlBehaviourType.isUserControlType(team.getControlType())) continue;
            LoggerHelper.methodInvocation(TAG, "init money text for " + team.getName() + " team");
            /*
                Object, which display money value to user. Only one such money text present in the screen
                because one device can't be used by multiple users to play.
            */
            MoneyText moneyText = new MoneyText(team.getName(),
                    getString(R.string.money_value_prefix), getVertexBufferObjectManager());
            mHud.attachChild(moneyText);
            final MovableUnitsLimitText limitText = new MovableUnitsLimitText(
                    moneyText.getX(), moneyText.getY() - 2 * moneyText.getFont().getLineHeight(),
                    getVertexBufferObjectManager());
            mHud.attachChild(limitText);
            final String key = ((Team) team).MOVABLE_UNIT_CREATED_CALLBACK_KEY;
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

    protected void initBotControlledTeam(final ITeam initializingTeam) {
        LoggerHelper.methodInvocation(TAG, "initBotControlledTeam");
        new Thread(new VeryFirstBot(initializingTeam, ClientGameActivity.this)).start();
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
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.FIRST_TEAM_ALLIANCE),
                getVertexBufferObjectManager());
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.SECOND_TEAM_ALLIANCE),
                getVertexBufferObjectManager());
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
    public synchronized void onEvent(final CreateMovableUnitEvent unitEvent) {
        final ITeam team = TeamsHolder.getInstance().getElement(unitEvent.getTeamName());
        //check units amount limit
        if (team.getUnitsAmount() >= Config.getConfig().getMovableUnitsLimit()) {
            return;
        }
        int unitKey = unitEvent.getKey();
        createMovableUnit(unitKey, team, unitEvent.isTopPath());
    }

    /** create unit with body and update it's enemies and moving path */
    protected MovableUnit createMovableUnit(int unitKey, final ITeam unitTeam, boolean isTopPath) {
        float x = unitTeam.getPlanet().getSpawnPointX(),
                y = unitTeam.getPlanet().getSpawnPointY();
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
        final Unit unit = unitTeam.getAlliance().getUnitDummy(unitKey).constructUnit();
        if (unitUniqueId.length > 0) {
            unit.setObjectUniqueId(unitUniqueId[0]);
        }
        unit.init(x, y, unitTeam.getName());
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
        final ITeam team = TeamsHolder.getInstance().getElement(unitEvent.getTeamName());
        StationaryUnit unit = (StationaryUnit) createUnit(unitKey, team,
                unitEvent.getX(), unitEvent.getY());
    }

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }
}