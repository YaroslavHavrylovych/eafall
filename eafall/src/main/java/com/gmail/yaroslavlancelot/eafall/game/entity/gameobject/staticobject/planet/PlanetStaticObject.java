package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.DefenceBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.SpecialBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.UnitBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.WealthBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.OffenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.IShipyard;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.ShipyardFactory;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.PlayerHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.ShowToastEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/** represent player planet */
public class PlanetStaticObject extends StaticObject implements IPlayerObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    /** current planet buildings */
    private final Map<Integer, IBuilding> mBuildings = new HashMap<>(15);
    /** current planet unit buildings (used for faster access to unit buildings) */
    private final List<IUnitBuilding> mUnitBuildings = new ArrayList<>(7);
    /** ships building and creation logic */
    private IShipyard mPlanetShipyard;
    /** current planet player name */
    private String mPlayerName;
    /** true - if it's left planet and false in other case */
    private boolean mIsLeft;
    /** exit with double click */
    private long mOnPlanetLastClick = 0;
    /** suppressor single click hint */
    private TimerHandler mSuppressorHintHandler;


    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion,
                              VertexBufferObjectManager objectManager) {
        super(x, y, SizeConstants.PLANET_DIAMETER, SizeConstants.PLANET_DIAMETER,
                textureRegion, objectManager);
        mIncomeIncreasingValue = 10;
        mObjectArmor = new Armor(Armor.ArmorType.MIXED.name(), 10);
        mIsLeft = x < SizeConstants.HALF_FIELD_WIDTH;
        mChildren = new SmartList<>(12);
        setSpriteGroupName(BatchingKeys.SUN_PLANET);
    }

    public boolean isLeft() {
        return mIsLeft;
    }

    public int getExistingBuildingsTypesAmount() {
        return mBuildings.size();
    }

    public Set<Integer> getExistingBuildingsTypes() {
        return mBuildings.keySet();
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

    public void init(String playerName, int planetNameRes, IUnitCreator creator, int objectMaximumHealth) {
        setPlayer(playerName);
        initHealthBar();
        initShipyard(playerName, creator);
        initHealth(objectMaximumHealth);
        initSuppressor(planetNameRes);
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
        LoggerHelper.methodInvocation(TAG, "createBuilding");
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
                                    getVertexBufferObjectManager(), mPlayerName);
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
            buildingStatObj.setPosition(
                    getX() + buildingStatObj.getX(), getY() + buildingStatObj.getY());
            mBuildings.put(buildingId.getId(), building);
        }
        boolean result = building.buyBuilding();
        if (result && building.getAmount() == 1) {
            building.getEntity().attachSelf();
        }
        return result;
    }

    /** Suppressor triggered by double click on the planet and kill all enemies on you side of the game field */
    private void initSuppressor(int planetNameRes) {
        initPlanetSingleClickHint(planetNameRes);
        setTouchCallback(new TouchHelper.UnboundedClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
                long time = System.currentTimeMillis();
                long delta = time - mOnPlanetLastClick;

                if (delta < TouchHelper.mMultipleClickTime) {
                    PlanetStaticObject.this.unregisterUpdateHandler(mSuppressorHintHandler);
                    //TODO activate suppressor
                    return;
                } else if (delta > TouchHelper.mMultipleClickDividerTime) {
                    PlanetStaticObject.this.unregisterUpdateHandler(mSuppressorHintHandler);
                    mSuppressorHintHandler.reset();
                    PlanetStaticObject.this.registerUpdateHandler(mSuppressorHintHandler);
                }
                mOnPlanetLastClick = time;
            }
        }));
    }

    private void initPlanetSingleClickHint(final int planetNameRes) {
        final IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        final int playerRes = player.getControlType().user() ?
                R.string.player_planet_text : R.string.opponent_planet_text;
        mSuppressorHintHandler = new TimerHandler(TouchHelper.mMultipleClickHintTime,
                new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        PlanetStaticObject.this.unregisterUpdateHandler(pTimerHandler);
                        EventBus.getDefault().post(new ShowToastEvent(true, playerRes, planetNameRes,
                                player.getAlliance().getAllianceStringRes()));
                    }
                });
    }

    /** get buildings amount for passed building type */
    public int getBuildingsAmount(int buildingId) {
        IBuilding building = mBuildings.get(buildingId);
        return building == null ? 0 : building.getAmount();
    }

    public IBuilding getBuilding(int id) {
        return mBuildings.get(id);
    }
}