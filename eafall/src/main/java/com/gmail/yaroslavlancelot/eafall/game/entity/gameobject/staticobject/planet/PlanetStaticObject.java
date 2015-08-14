package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.CreepBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.DefenceBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.ICreepBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.SpecialBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.WealthBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.IShipyard;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards.ShipyardFactory;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.PlayerHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** represent player planet */
public class PlanetStaticObject extends StaticObject implements IPlayerObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    /** current planet buildings */
    private final Map<Integer, IBuilding> mBuildings = new HashMap<>(15);
    /** current planet creep buildings (used for faster access to creep buildings) */
    private final List<ICreepBuilding> mCreepBuildings = new ArrayList<>(7);
    /** ships building and creation logic */
    private IShipyard mPlanetShipyard;
    /** current planet player name */
    private String mPlayerName;
    private boolean mIsLeft;


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
    public String getPlayer() {
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

    public void init(String playerName, int objectMaximumHealth) {
        setPlayer(playerName);
        initHealthBar();
        initShipyard(playerName);
        initHealth(objectMaximumHealth);
    }

    public void initShipyard(String playerName) {
        mPlanetShipyard = ShipyardFactory.getShipyard((int) getX(), (int) getY(), playerName);
        registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mPlanetShipyard.update(mCreepBuildings);
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
                    ICreepBuilding creepBuilding =
                            new CreepBuilding((CreepBuildingDummy) buildingDummy,
                                    getVertexBufferObjectManager(), mPlayerName);
                    mCreepBuildings.add(creepBuilding);
                    building = creepBuilding;
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
            attachChild(building.getEntity());
        }
        return result;
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