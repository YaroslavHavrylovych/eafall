package com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.SuppressorSoundableAnimation;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.DefenceBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.SpecialBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.UnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.WealthBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.OffenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.Selectable;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.IShipyard;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.ShipyardFactory;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.yaroslavlancelot.eafall.game.entity.health.PlayerHealthBar;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ShowHudTextEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/** represent player planet */
public abstract class PlanetStaticObject extends StaticObject implements IPlayerObject, Selectable {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    /** planet explosion sound path */
    public static final String PLANET_EXPLOSION_SOUND = "audio/sound/boom/big_bada_boom.ogg";
    /** current planet buildings */
    private final Map<Integer, IBuilding> mBuildings = new HashMap<>(15);
    /** current planet unit buildings (used for faster access to unit buildings) */
    private final List<IUnitBuilding> mUnitBuildings = new ArrayList<>(7);
    /** ships building and creation logic */
    private IShipyard mPlanetShipyard;
    /** current planet player name */
    private String mPlayerName;
    /** used to prevent click trigger if double click operation were performed */
    private TimerHandler mClickHandler;
    /** contains true if suppressor was used */
    private boolean mIsSuppressorUsed = false;

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion,
                              VertexBufferObjectManager objectManager) {
        super(x, y, SizeConstants.PLANET_DIAMETER, SizeConstants.PLANET_DIAMETER,
                textureRegion, objectManager);
        mIncomeIncreasingValue = 10;
        mObjectArmor = new Armor(Armor.ArmorType.MIXED.name(), 10);
        mChildren = new SmartList<>(12);
        if (!isLeft()) {
            setFlippedHorizontal(true);
        }
        setSpriteGroupName(BatchingKeys.SUN_PLANET);
    }

    public boolean isLeft() {
        return isLeft(mX);
    }

    public int getExistingBuildingsTypesAmount() {
        return mBuildings.size();
    }

    public Set<Integer> getExistingBuildingsTypes() {
        return mBuildings.keySet();
    }

    /** @return true if suppressor was used and false in other case */
    public boolean isSuppressorUsed() {
        return mIsSuppressorUsed;
    }

    @Override
    public int getIncome() {
        int value = super.getIncome();
        int percentIncrease = 0;
        for (IBuilding building : mBuildings.values()) {
            if (building.getBuildingType() == BuildingType.SPECIAL_BUILDING) {
                continue;
            }
            if (building.getBuildingType() == BuildingType.WEALTH_BUILDING) {
                percentIncrease += building.getIncome();
                continue;
            }
            value += building.getIncome();
        }
        return value + (int) (value * (((float) percentIncrease) / 100));
    }

    @Override
    protected void onNegativeHealth() {
        for (IBuilding building : mBuildings.values()) {
            building.setIgnoreUpdates(true);
        }
        mPhysicBody.setActive(false);
        final AnimatedSprite animatedSprite = new AnimatedSprite(mX, mY,
                (ITiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.KEY_PLANET_EXPLOSION),
                getVertexBufferObjectManager());
        animatedSprite.animate(81, false, new AnimatedSprite.IAnimationListener() {
            @Override
            public void onAnimationStarted(final AnimatedSprite pAnimatedSprite, final int pInitialLoopCount) {
                SoundFactory.getInstance().playSound(PLANET_EXPLOSION_SOUND);
                destroy();
            }

            @Override
            public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex) {
            }

            @Override
            public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount) {
            }

            @Override
            public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
                animatedSprite.detachSelf();
            }
        });
        SpriteGroupHolder.getGroup(BatchingKeys.EXPLOSIONS).attachChild(animatedSprite);
    }

    @Override
    public void destroy() {
        for (IBuilding building : mBuildings.values()) {
            building.destroy();
        }
        super.destroy();
    }

    @Override
    public String getPlayerName() {
        return mPlayerName;
    }

    @Override
    public void setPlayer(String playerName) {
        mPlayerName = playerName;
    }

    @Override
    protected IHealthBar createHealthBar() {
        return new PlayerHealthBar(mPlayerName, getVertexBufferObjectManager(),
                PathHelper.isLtrPath(getX()));
    }

    public abstract void registerTouch(IEntity entity);

    public abstract void unregisterTouch(IEntity entity);

    public void init(String playerName, int planetNameRes, IUnitCreator creator, int objectMaximumHealth) {
        setPlayer(playerName);
        initHealthBar();
        initShipyard(playerName, creator);
        initHealth(objectMaximumHealth);
        initTouchCallbacks(planetNameRes);
    }

    public void initShipyard(String playerName, IUnitCreator creator) {
        mPlanetShipyard = ShipyardFactory.getShipyard((int) getX(), (int) getY(), playerName, creator);
        registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mPlanetShipyard.update(mUnitBuildings);
            }
        }));
    }

    /**
     * Invoke if you want to create new building. If building is doing some real action
     * (it's on the server side or it's single player and not an client which just is showing)
     * then player money will be reduced.
     *
     * @param buildingId id of the building you want to create
     * @return true if building amount was increased and false in other case
     */
    public boolean createBuilding(BuildingId buildingId) {
        IBuilding building = mBuildings.get(buildingId.getId());
        if (building == null) {
            final BuildingDummy buildingDummy = PlayersHolder.getPlayer(mPlayerName).getAlliance()
                    .getBuildingDummy(buildingId);
            if (buildingDummy == null) {
                throw new IllegalArgumentException("no building with id " + buildingId);
            }

            switch (buildingDummy.getBuildingType()) {
                case CREEP_BUILDING: {
                    IUnitBuilding unitBuilding =
                            new UnitBuilding((OffenceBuildingDummy) buildingDummy,
                                    getVertexBufferObjectManager(), mPlayerName) {
                                @Override
                                public void registerTouch(final IEntity entity) {
                                    PlanetStaticObject.this.registerTouch(entity);
                                }

                                @Override
                                public void unregisterTouch(final IEntity entity) {
                                    PlanetStaticObject.this.unregisterTouch(entity);
                                }
                            };
                    mUnitBuildings.add(unitBuilding);
                    building = unitBuilding;
                    break;
                }
                case WEALTH_BUILDING: {
                    building = new WealthBuilding((WealthBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mPlayerName);
                    break;
                }
                case SPECIAL_BUILDING: {
                    building = new SpecialBuilding((SpecialBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mPlayerName);
                    break;
                }
                case DEFENCE_BUILDING: {
                    building = new DefenceBuilding((DefenceBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mPlayerName);
                    break;
                }
                default: {
                    throw new IllegalStateException("unknown building type in create building");
                }
            }
            StaticObject buildingStatObj = building.getEntity();
            buildingStatObj.setSpriteGroupName(BatchingKeys.getBuildingSpriteGroup(mPlayerName));
            float xOffset = isLeft() ? buildingStatObj.getX() : -buildingStatObj.getX();
            buildingStatObj.setPosition(
                    getX() + xOffset, getY() + buildingStatObj.getY());
            mBuildings.put(buildingId.getId(), building);
        }
        boolean result = building.buyBuilding();
        if (result && building.getAmount() == 1) {
            StaticObject stObj = building.getEntity();
            stObj.attachSelf();
            registerTouch(stObj);
        }
        return result;
    }

    /** Suppressor triggered by double click on the planet and kill all enemies on you side of the game field */
    private void initTouchCallbacks(final int planetNameRes) {
        mClickHandler = new TimerHandler(
                TouchHelper.UnboundedSelectorEvents.DOUBLE_CLICK_TRIGGER_MILLIS * 1f / 1000f,
                false, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                final IPlayer player = PlayersHolder.getPlayer(mPlayerName);
                final int playerRes = player.getControlType().user() ?
                        R.string.player_planet_text : R.string.opponent_planet_text;
                EventBus.getDefault().post(new ShowHudTextEvent(playerRes, planetNameRes,
                        player.getAlliance().getAllianceStringRes(),
                        mIsSuppressorUsed ? R.string.used : R.string.unused));
            }
        });
        mClickHandler.setTimerCallbackTriggered(true);
        registerUpdateHandler(mClickHandler);
        setTouchCallback(new TouchHelper.UnboundedSelectorEvents(this) {
            @Override
            public void click() {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.SELECT);
                mClickHandler.reset();
            }

            @Override
            public void doubleClick() {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.SELECT);
                mClickHandler.setTimerCallbackTriggered(true);
                if (PlayersHolder.getPlayer(mPlayerName).getControlType().user()) {
                    RollingPopupManager.getInstance().getPopup(ConstructionsPopup.KEY).triggerPopup();
                }
            }

            @Override
            public void holdClick() {
                if (PlayersHolder.getPlayer(mPlayerName).getControlType().user()) {
                    useSuppressor();
                } else {
                    EventBus.getDefault().post(new ShowHudTextEvent(R.string.wrong_planet_suppressor));
                }
            }
        });
    }

    /** get buildings amount for passed building type */
    public int getBuildingsAmount(int buildingId) {
        IBuilding building = mBuildings.get(buildingId);
        return building == null ? 0 : building.getAmount();
    }

    /** force planet to use the suppressor */
    public void useSuppressor() {
        if (!mIsSuppressorUsed) {
            mIsSuppressorUsed = true;
            IPlayer yourPlayer = PlayersHolder.getPlayer(mPlayerName);
            //text
            if (yourPlayer.getControlType().user()) {
                EventBus.getDefault().post(new ShowHudTextEvent(R.string.suppressor_being_used));
            }
            //animation and sound
            SuppressorSoundableAnimation suppressor = new SuppressorSoundableAnimation(mX, mY, getVertexBufferObjectManager());
            IEntity parent = getParent();
            if (parent != null) {
                parent = parent.getParent();
                parent.attachChild(suppressor);
                suppressor.startAnimation(!isLeft());
            }
            //death
            IPlayer enemy = yourPlayer.getEnemyPlayer();
            List<Unit> enemies = enemy.getUnitMap().getUnitOnSide(isLeft());
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).kill();
            }
        }
    }

    public IBuilding getBuilding(int id) {
        return mBuildings.get(id);
    }

    public static boolean isLeft(float x) {
        return x < SizeConstants.HALF_FIELD_WIDTH;
    }
}