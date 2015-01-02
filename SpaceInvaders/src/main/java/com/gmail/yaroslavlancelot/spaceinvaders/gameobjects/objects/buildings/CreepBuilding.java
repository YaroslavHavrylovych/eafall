package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

public class CreepBuilding extends Building implements ICreepBuilding {
    private static final String TAG = CreepBuilding.class.getCanonicalName();
    /** displayed on the planet building */
    private StaticObject mBuilding;
    /** building dummy link */
    private CreepBuildingDummy mCreepBuildingDummy;
    /** create units */
    private UnitCreatorCycle mUnitCreatorCycle;
    /** building produce units which will go by the top path */
    private boolean mIsTopPath = true;

    public CreepBuilding(final CreepBuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        super(BuildingType.CREEP_BUILDING, teamName);
        mCreepBuildingDummy = dummy;
        float width = dummy.getWidth();
        float height = dummy.getHeight();

        // init first creep building
        Color teamColor = TeamsHolder.getTeam(teamName).getTeamColor();
        mBuilding = getBuildingByUpgrade(mUpgrade, dummy, teamColor, objectManager);

        // attach the building to the basement
        Rectangle basement = new Rectangle(dummy.getX(), dummy.getY(), width, height, objectManager);
        basement.setColor(Color.TRANSPARENT);
        setEntity(basement);
    }

    private static StaticObject getBuildingByUpgrade(final int upgrade, final CreepBuildingDummy creepBuildingDummy,
                                                     final Color teamColor,
                                                     VertexBufferObjectManager objectManager) {
        return new StaticObject(0, 0, creepBuildingDummy.getTextureRegionArray(upgrade), objectManager) {
            {
                setCost(creepBuildingDummy.getCost(upgrade));
                setIncome((int) (getCost() * 0.03));
                setWidth(creepBuildingDummy.getWidth());
                setHeight(creepBuildingDummy.getWidth());
                setObjectStringId(creepBuildingDummy.getStringId());
                setBackgroundArea(creepBuildingDummy.getTeamColorAreaArray(upgrade));
                setBackgroundColor(teamColor);
            }
        };
    }

    @Override
    public synchronized boolean buyBuilding() {
        ITeam team = TeamsHolder.getTeam(mTeamName);
        boolean isFakePlanet = TeamControlBehaviourType.isClientSide(team.getTeamControlType());
        if (isFakePlanet) {
            mBuildingsAmount++;
        } else {
            int cost = mCreepBuildingDummy.getCost(mUpgrade);
            if (team.getMoney() < cost) {
                return false;
            }
            team.changeMoney(-cost);

            if (mBuildingsAmount <= 0) {
                mBuildingsAmount = 0;
                getEntity().attachChild(mBuilding);
                mUnitCreatorCycle = new UnitCreatorCycle(mTeamName,
                        mCreepBuildingDummy.getUnitId(mUpgrade), isTopPath());
                mBuilding.registerUpdateHandler(new TimerHandler(
                        mCreepBuildingDummy.getUnitCreationTime(mUpgrade), true, mUnitCreatorCycle));
            }
            mBuildingsAmount++;
            mUnitCreatorCycle.increaseUnitsAmount();
        }
        setIncome(mBuildingsAmount * mBuilding.getIncome());
        EventBus.getDefault().post(new BuildingsAmountChangedEvent(mTeamName,
                BuildingId.makeId(mCreepBuildingDummy.getBuildingId(), mUpgrade),
                mBuildingsAmount));
        return true;
    }

    @Override
    public synchronized boolean upgradeBuilding() {
        //check upgrade for existence and buildings amount
        int nextUpgrade = mUpgrade + 1;
        if (nextUpgrade >= mCreepBuildingDummy.getUpgrades()) {
            throw new UnsupportedOperationException("Building upgrade exceed possible building upgrades");
        }
        if (mBuildingsAmount <= 0) {
            return false;
        }

        ITeam team = TeamsHolder.getTeam(mTeamName);
        boolean isFakePlanet = TeamControlBehaviourType.isClientSide(team.getTeamControlType());
        if (!isFakePlanet) {
            int cost = mCreepBuildingDummy.getCost(nextUpgrade);
            //check money
            if (team.getMoney() < cost) {
                return false;
            }
            //upgrade
            team.changeMoney(-cost);
            mBuilding.clearUpdateHandlers();
            mUnitCreatorCycle = new UnitCreatorCycle(mTeamName, mCreepBuildingDummy.getUnitId(nextUpgrade),
                    mBuildingsAmount, isTopPath());
        }
        Color teamColor = TeamsHolder.getTeam(mTeamName).getTeamColor();
        VertexBufferObjectManager objectManager = mBuilding.getVertexBufferObjectManager();
        mBuilding = getBuildingByUpgrade(nextUpgrade, mCreepBuildingDummy, teamColor, objectManager);
        if (!isFakePlanet) {
            mBuilding.registerUpdateHandler(new TimerHandler(20, true, mUnitCreatorCycle));
        }

        getEntity().detachChildren();
        getEntity().attachChild(mBuilding);

        mUpgrade = nextUpgrade;
        //change description popup
        EventBus.getDefault().post(new BuildingDescriptionShowEvent(
                BuildingId.makeId(mCreepBuildingDummy.getBuildingId(), nextUpgrade), mTeamName));
        return true;
    }

    @Override
    public boolean isTopPath() {
        return mIsTopPath;
    }

    @Override
    public void setPath(boolean isTop) {
        mIsTopPath = isTop;
        if (mUnitCreatorCycle != null) {
            mUnitCreatorCycle.setUnitMovementPath(mIsTopPath);
        }
    }
}
