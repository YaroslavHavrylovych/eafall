package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

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
    /** building basement, holder for all building elements */
    private Rectangle mBasement;

    public Building(BuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        mBuildingType = dummy.getBuildingType();
        mTeamName = teamName;
        mDummy = dummy;

        float width = dummy.getWidth();
        float height = dummy.getHeight();

        // init first creep building
        Color teamColor = TeamsHolder.getTeam(teamName).getTeamColor();
        mBuildingStaticObject = getBuildingByUpgrade(mUpgrade, dummy, teamColor, objectManager);

        // attach the building to the basement
        Rectangle basement = new Rectangle(dummy.getX(), dummy.getY(), width, height, objectManager);
        basement.setColor(Color.TRANSPARENT);
        setEntity(basement);
    }

    protected static StaticObject getBuildingByUpgrade(final int upgrade, final BuildingDummy buildingDummy,
                                                       final Color teamColor,
                                                       VertexBufferObjectManager objectManager) {
        return new StaticObject(0, 0, buildingDummy.getTextureRegionArray(upgrade), objectManager) {
            {
                setCost(buildingDummy.getCost(upgrade));
                setIncome((int) (getCost() * 0.03));
                setWidth(buildingDummy.getWidth());
                setHeight(buildingDummy.getWidth());
                setObjectStringId(buildingDummy.getStringId());
                setBackgroundArea(buildingDummy.getTeamColorAreaArray(upgrade));
                setBackgroundColor(teamColor);
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
        return mBasement;
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
            getEntity().attachChild(mBuildingStaticObject);
        }
        mBuildingsAmount++;
        EventBus.getDefault().post(new BuildingsAmountChangedEvent(mTeamName,
                BuildingId.makeId(mDummy.getBuildingId(), mUpgrade),
                mBuildingsAmount));
        return true;
    }

    /** set entity for the current building (in general it's basement) */
    protected void setEntity(Rectangle basement) {
        mBasement = basement;
    }

    protected void setIncome(int income) {
        mIncome = income;
    }

    protected void setBuildingsAmount(int buildingsAmount) {
        mBuildingsAmount = buildingsAmount;
    }
}
