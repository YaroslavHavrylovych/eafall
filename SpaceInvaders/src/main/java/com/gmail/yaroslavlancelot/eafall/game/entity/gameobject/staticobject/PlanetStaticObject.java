package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.CreepBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.DefenceBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.SpecialBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.WealthBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.health.IHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** represent player planet */
public class PlanetStaticObject extends StaticObject implements IPlayerObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private Map<Integer, IBuilding> mBuildings = new HashMap<Integer, IBuilding>(10);
    /** current planet player name */
    private String mPlayerName;


    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion,
                              VertexBufferObjectManager objectManager) {
        super(x, y, SizeConstants.PLANET_DIAMETER, SizeConstants.PLANET_DIAMETER,
                textureRegion, objectManager);
        mIncomeIncreasingValue = 10;
        mObjectArmor = new Armor(Armor.ArmorType.MIXED.name(), 10);
        initSpawnPoint();
        mChildren = new SmartList<BatchedSprite>(12);
        setSpriteGroupName(BatchingKeys.SUN_PLANET);
    }

    public float getSpawnPointX() {
        return mSpawnPointX;
    }

    public float getSpawnPointY() {
        return mSpawnPointY;
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
        initHealthBar();
    }

    @Override
    protected IHealthBar createHealthBar() {
        return null;
    }

    private void initSpawnPoint() {
        float x;
        if (getX() < SizeConstants.HALF_FIELD_WIDTH) {
            x = SizeConstants.PLANET_DIAMETER + SizeConstants.ADDITION_MARGIN_FOR_PLANET + SizeConstants.UNIT_SIZE;
        } else {
            x = getX() - SizeConstants.PLANET_DIAMETER / 2
                    - SizeConstants.UNIT_SIZE - SizeConstants.ADDITION_MARGIN_FOR_PLANET;
        }
        setSpawnPoint(x, getY());
    }

    /** set unit spawn point */
    public void setSpawnPoint(float spawnPointX, float spawnPointY) {
        mSpawnPointX = spawnPointX;
        mSpawnPointY = spawnPointY;
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
                    building = new CreepBuilding((CreepBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mPlayerName);
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
        return building.buyBuilding();
    }

    /** get buildings amount for passed building type */
    public int getBuildingsAmount(int buildingId) {
        IBuilding buildings = mBuildings.get(buildingId);
        if (buildings == null) return 0;
        return buildings.getAmount();
    }

    public IBuilding getBuilding(int id) {
        return mBuildings.get(id);
    }
}