package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.loops.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

public class CreepBuilding extends Building implements ICreepBuilding {
    private static final String TAG = CreepBuilding.class.getCanonicalName();
    /** building dummy link */
    private CreepBuildingDummy mCreepBuildingDummy;
    /** create units */
    private UnitCreatorCycle mUnitCreatorCycle;
    /** building produce units which will go by the top path */
    private boolean mIsTopPath = true;

    public CreepBuilding(final CreepBuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        super(dummy, objectManager, teamName);
        mCreepBuildingDummy = dummy;
    }

    @Override
    public synchronized boolean buyBuilding() {
        boolean result = super.buyBuilding();
        ITeam team = TeamsHolder.getTeam(mTeamName);
        boolean isFakePlanet = TeamControlBehaviourType.isClientSide(team.getTeamControlType());
        //building was created
        if (isFakePlanet || result) {
            setIncome(mBuildingsAmount * mBuildingStaticObject.getIncome());
            if (isFakePlanet) {
                return true;
            }
        }

        //building wasn't created
        if (!result) {
            return false;
        }

        //first building created
        if (mUnitCreatorCycle == null) {
            mUnitCreatorCycle = new UnitCreatorCycle(mTeamName,
                    mCreepBuildingDummy.getUnitId(mUpgrade), isTopPath());
            mBuildingStaticObject.registerUpdateHandler(new TimerHandler(
                    mCreepBuildingDummy.getUnitCreationTime(mUpgrade), true, mUnitCreatorCycle));
        }

        //successful creation should increase produced creeps
        mUnitCreatorCycle.increaseUnitsAmount();

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
            mBuildingStaticObject.clearUpdateHandlers();
            mUnitCreatorCycle = new UnitCreatorCycle(mTeamName, mCreepBuildingDummy.getUnitId(nextUpgrade),
                    mBuildingsAmount, isTopPath());
        }
        Color teamColor = TeamsHolder.getTeam(mTeamName).getTeamColor();
        VertexBufferObjectManager objectManager = mBuildingStaticObject.getVertexBufferObjectManager();
        mBuildingStaticObject = getBuildingByUpgrade(nextUpgrade, mCreepBuildingDummy, teamColor, objectManager);
        if (!isFakePlanet) {
            mBuildingStaticObject.registerUpdateHandler(new TimerHandler(20, true, mUnitCreatorCycle));
        }

        getEntity().detachChildren();
        getEntity().attachChild(mBuildingStaticObject);

        mUpgrade = nextUpgrade;
        //change description popup
        EventBus.getDefault().post(new BuildingDescriptionShowEvent(
                BuildingId.makeId(mCreepBuildingDummy.getBuildingId(), nextUpgrade), mTeamName));
        return true;
    }
}
