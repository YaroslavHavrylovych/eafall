package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import de.greenrobot.event.EventBus;

/** general buildings functionality */
public abstract class Building implements IBuilding {
    /** current building team name */
    protected final String mTeamName;
    /** current building group type */
    private final BuildingType mBuildingType;
    /** current building dummy */
    protected BuildingDummy mDummy;
    /** amount of buildings of the current building type */
    protected int mBuildingsAmount;
    /** current building upgrade */
    protected int mUpgrade;
    /** displayed on the planet building */
    protected StaticObject mBuildingStaticObject;
    /** income which give all buildings of the current type (can be in percents) */
    private int mIncome;

    public Building(BuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        mBuildingType = dummy.getBuildingType();
        mTeamName = teamName;
        mDummy = dummy;
        // init first creep building
        Color teamColor = TeamsHolder.getTeam(teamName).getTeamColor();
        mBuildingStaticObject = getBuildingByUpgrade(mUpgrade, dummy, teamColor, objectManager);
    }

    protected static StaticObject getBuildingByUpgrade(final int upgrade, final BuildingDummy buildingDummy,
                                                       final Color teamColor,
                                                       VertexBufferObjectManager objectManager) {
        return new StaticObject(buildingDummy.getX(), buildingDummy.getY(),
                buildingDummy.getTextureRegionArray(upgrade), objectManager) {
            {
                setCost(buildingDummy.getCost(upgrade));
                setIncome((int) (getCost() * 0.03));
                setWidth(buildingDummy.getWidth());
                setHeight(buildingDummy.getWidth());
                setObjectStringId(buildingDummy.getStringId());
            }
        };
    }

    @Override
    public BuildingType getBuildingType() {
        return mBuildingType;
    }

    @Override
    public int getIncome() {
        return mIncome;
    }

    @Override
    public int getAmount() {
        return mBuildingsAmount;
    }

    @Override
    public IEntity getEntity() {
        return mBuildingStaticObject;
    }

    @Override
    public int getUpgrade() {
        return mUpgrade;
    }

    @Override
    public boolean buyBuilding() {
        ITeam team = TeamsHolder.getTeam(mTeamName);
        if (!TeamControlBehaviourType.isClientSide(team.getTeamControlType())) {
            int cost = mDummy.getCost(mUpgrade);
            if (team.getMoney() < cost) {
                return false;
            }
            team.changeMoney(-cost);
        }
        if (mBuildingsAmount <= 0) {
            mBuildingsAmount = 0;
        }
        mBuildingsAmount++;
        EventBus.getDefault().post(new BuildingsAmountChangedEvent(mTeamName,
                BuildingId.makeId(mDummy.getBuildingId(), mUpgrade),
                mBuildingsAmount));
        return true;
    }

    @Override
    public float getX() {
        return mDummy.getX();
    }

    @Override
    public float getY() {
        return mDummy.getY();
    }

    protected void setIncome(int income) {
        mIncome = income;
    }

    protected void setBuildingsAmount(int buildingsAmount) {
        mBuildingsAmount = buildingsAmount;
    }
}
