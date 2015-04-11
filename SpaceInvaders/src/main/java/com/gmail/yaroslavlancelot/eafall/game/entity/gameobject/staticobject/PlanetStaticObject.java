package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.ITeamObject;
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
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.entity.IEntity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** represent team planet */
public class PlanetStaticObject extends StaticObject implements ITeamObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private Map<Integer, IBuilding> mBuildings = new HashMap<Integer, IBuilding>(10);
    /** current planet team name */
    private String mTeamName;


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

    public float getSpawnPointX() {
        return mSpawnPointX;
    }

    public float getSpawnPointY() {
        return mSpawnPointY;
    }

    /**
     * Invoke if you want to create new building. If building is doing some real action
     * (it's on the server side or it's single player and not an client which just is showing)
     * then team money will be reduced.
     *
     * @param buildingId id of the building you want to create
     * @return true if building amount was increased and false in other case
     */
    public boolean createBuilding(BuildingId buildingId) {
        LoggerHelper.methodInvocation(TAG, "createBuilding");
        IBuilding building = mBuildings.get(buildingId.getId());
        if (building == null) {
            final BuildingDummy buildingDummy = TeamsHolder.getTeam(mTeamName).getTeamRace()
                    .getBuildingDummy(buildingId);
            if (buildingDummy == null) {
                throw new IllegalArgumentException("no building with id " + buildingId);
            }

            switch (buildingDummy.getBuildingType()) {
                case CREEP_BUILDING: {
                    building = new CreepBuilding((CreepBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mTeamName);
                    break;
                }
                case WEALTH_BUILDING: {
                    building = new WealthBuilding((WealthBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mTeamName);
                    break;
                }
                case SPECIAL_BUILDING: {
                    building = new SpecialBuilding((SpecialBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mTeamName);
                    break;
                }
                case DEFENCE_BUILDING: {
                    building = new DefenceBuilding((DefenceBuildingDummy) buildingDummy,
                            getVertexBufferObjectManager(), mTeamName);
                    break;
                }
                default: {
                    throw new IllegalStateException("unknown building type in create building");
                }
            }
            IEntity entity = building.getEntity();
            entity.setPosition(getX() + entity.getX(), getY() + entity.getY());
            attachChild(entity);
            mBuildings.put(buildingId.getId(), building);
        }
        return building.buyBuilding();
    }

    /** get buildings amount for passed building type */
    public int getBuildingsAmount(int buildingId) {
        IBuilding buildings = mBuildings.get(buildingId);
        if (buildings == null) return 0;
        return buildings.getAmount();
    }

    public int getExistingBuildingsTypesAmount() {
        return mBuildings.size();
    }

    public Set<Integer> getExistingBuildingsTypes() {
        return mBuildings.keySet();
    }

    public IBuilding getBuilding(int id) {
        return mBuildings.get(id);
    }

    @Override
    public String getTeam() {
        return mTeamName;
    }

    @Override
    public void setTeam(String teamName) {
        mTeamName = teamName;
        initHealthBar();
    }
}