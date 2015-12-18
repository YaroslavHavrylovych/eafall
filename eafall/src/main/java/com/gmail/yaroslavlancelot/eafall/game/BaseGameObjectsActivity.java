package com.gmail.yaroslavlancelot.eafall.game;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.android.dialog.SettingsDialog;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletsPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.explosion.UnitExplosionPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.TwoWaysUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AbstractSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.DetachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.PauseGameEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.ShowSettingsEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit.CreateDefenceUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.IPeriodic;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.unit.UnitPositionUpdater;
import com.gmail.yaroslavlancelot.eafall.game.mission.MissionIntent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.game.BaseGameObjectsLoader;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base for the game and sandbox: players initialization, units, background, partly lifecycle.
 * <br/>
 * No star, no planets.
 */
public abstract class BaseGameObjectsActivity extends EaFallActivity implements IUnitCreator {
    /** tag, which is used for debugging purpose */
    public static final String TAG = BaseGameObjectsActivity.class.getCanonicalName();
    /** contains whole game units/warriors */
    protected final Map<Long, GameObject> mGameObjectsMap = new HashMap<>();
    /** first player */
    protected IPlayer mSecondPlayer;
    /** second player */
    protected IPlayer mFirstPlayer;
    /** current game physics world */
    protected PhysicsWorld mPhysicsWorld;
    /** current mission/game config */
    protected MissionConfig mMissionConfig;
    /** game cycles (e.g. money increase, timer etc) */
    protected List<IPeriodic> mGamePeriodic = new ArrayList<>(2);

    @Override
    public OffenceUnit createMovableUnit(IPlayer unitPlayer,
                                         int unitKey, int x, int y, boolean isTopPath) {
        IUnitPath unitPath = new TwoWaysUnitPath(PathHelper.isLtrPath(x), isTopPath);
        return createMovableUnit(unitPlayer, unitKey, x, y, unitPath);
    }


    public OffenceUnit createMovableUnit(IPlayer unitPlayer, int unitKey, int x, int y, IUnitPath unitPath) {
        OffenceUnit offenceUnit = (OffenceUnit) createUnit(unitKey, unitPlayer, x, y);
        offenceUnit.setUnitPath(unitPath);
        return offenceUnit;
    }

    @Override
    public Body createPhysicBody(BodiedSprite bodiedSprite, BodyDef.BodyType bodyType,
                                 FixtureDef fixtureDef) {
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, bodiedSprite, bodyType, fixtureDef);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bodiedSprite, body, true, false));
        bodiedSprite.setBody(body);
        return body;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        EngineOptions engineOptions = super.onCreateEngineOptions();
        mMissionConfig = getIntent().getExtras().getParcelable(MissionIntent.MISSION_CONFIG);
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false, 2, 2);
        return engineOptions;
    }

    @Override
    public void onPauseGame() {
        if (GameState.isResourcesLoaded()) {
            pause(true);
        }
        super.onPauseGame();
    }

    @Override
    public void finish() {
        pause(true);
        super.finish();
    }

    @Override
    protected String createMusicPath() {
        return StringConstants.getMusicPath() + "background_1.ogg";
    }

    @Override
    protected void preResourcesLoading() {
        //alliance and player
        createAlliances();
        createPlayers();
    }

    @Override
    protected void loadResources() {
        //has to be before res loading as it sets units amount which used by res loader
        ((BaseGameObjectsLoader) mResourcesLoader)
                .setMovableUnitsLimit(mMissionConfig.getMovableUnitsLimit());
        //resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        scene.setBackground(StringConstants.FILE_BACKGROUND, getVertexBufferObjectManager());
        mSceneManager.getWorkingScene().registerUpdateHandler(mPhysicsWorld);
        //attach SpriteGroups to the scene
        SpriteGroupHolder.attachSpriteGroups(scene, BatchingKeys.BatchTag.GAME_SCENE.value());
        //hud
        mHud.initHudElements(mCamera, getVertexBufferObjectManager(), mMissionConfig);
        //pools
        BulletsPool.init(getVertexBufferObjectManager());
        UnitExplosionPool.init(getVertexBufferObjectManager());
        //sound
        ICameraHandler cameraHandler = mSceneManager.getWorkingScene().getCameraHandler();
        SoundFactory.getInstance().setCameraHandler(cameraHandler);
    }

    @Override
    protected void onShowWorkingScene() {
        startPeriodic();
    }

    /**
     * set game to pause if pause parameter equals to true and resume the game if false.
     * <p/>
     * Uses {@link #setState(GameState.State)} and one out of two possible params
     * (GameState.State.RESUMED or GameState.State.PAUSED)
     */
    protected void pause(boolean pause) {
        setState(pause ? GameState.State.PAUSED : GameState.State.RESUMED);
    }

    /**
     * registers all update handlers from #mGamePeriodic
     * <br/>
     * e.g. money update events, game time ticking events
     */
    protected void startPeriodic() {
        for (IPeriodic periodic : mGamePeriodic) {
            mSceneManager.getWorkingScene().registerUpdateHandler(periodic.getUpdateHandler());
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
        //units map
        mFirstPlayer.createUnitsMap(true);
        mSecondPlayer.createUnitsMap(false);
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
    protected void initPlayer(IPlayer player) {
        initPlayerFixtureDef(player);
        PlayersHolder.getInstance().addElement(player.getName(), player);
    }

    protected void initPlayerFixtureDef(IPlayer player) {
        boolean isRemote = player.getControlType().clientSide();
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
        IPlayer player = createPlayer(playerNameInExtra, alliance, playerType, mMissionConfig);
        mGamePeriodic.add(new UnitPositionUpdater(player));
        return player;
    }

    /**
     * Different game types have different player creation parameters. In our case it's
     * a chance to get money out of the dead unit. We can define it only on later stages.
     */
    protected abstract IPlayer createPlayer(String name, IAlliance alliance,
                                            IPlayer.ControlType playerType,
                                            MissionConfig missionConfig);

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

    @SuppressWarnings("unused")
    /** stop the game engine */
    public void onEvent(PauseGameEvent pauseGameEvent) {
        pause(pauseGameEvent.isPause());
    }

    /** attach sprite to entity (sprite group, hud or game scene) */
    private void attachSprite(final Sprite sprite, final Entity parent) {
        parent.attachChild(sprite);
    }

    /**
     * Parsing alliances from the intent and save to
     * {@link com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder}
     */
    protected void createAlliances() {
        Intent intent = getIntent();
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.FIRST_PLAYER_ALLIANCE),
                getVertexBufferObjectManager());
        AllianceHolder.addAllianceByName(intent.getStringExtra(StringConstants.SECOND_PLAYER_ALLIANCE),
                getVertexBufferObjectManager());
    }

    /** create unit */
    protected Unit createUnit(int unitKey, final IPlayer unitPlayer, float x, float y) {
        return createThinUnit(unitKey, unitPlayer, x, y);
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
        unit.init(x, y, this);
        mGameObjectsMap.put(unit.getObjectUniqueId(), unit);
        return unit;
    }

    /** attach entity to game scene */
    protected void attachSprite(final BatchedSprite batchedSprite) {
        attachSprite(batchedSprite, SpriteGroupHolder.getGroup(batchedSprite.getSpriteGroupName()));
    }

    @SuppressWarnings("unused")
    public void onEvent(ShowSettingsEvent event) {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getSupportFragmentManager(), SettingsDialog.KEY);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateDefenceUnitEvent unitEvent) {
        int unitKey = unitEvent.getKey();
        final IPlayer player = PlayersHolder.getPlayer(unitEvent.getPlayerName());
        DefenceUnit unit = (DefenceUnit) createUnit(unitKey, player,
                unitEvent.getX(), unitEvent.getY());
    }

    /** return unit if it exist (live) by using unit unique id */
    protected GameObject getGameObjectById(long id) {
        return mGameObjectsMap.get(id);
    }
}