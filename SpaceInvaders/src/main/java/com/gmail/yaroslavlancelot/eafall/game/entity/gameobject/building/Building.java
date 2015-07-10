package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** general buildings functionality */
public abstract class Building implements IBuilding {
    /** current building player name */
    protected final String mPlayerName;
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

    public Building(BuildingDummy dummy, VertexBufferObjectManager objectManager, String playerName) {
        mBuildingType = dummy.getBuildingType();
        mPlayerName = playerName;
        mDummy = dummy;
        // init first creep building
        mBuildingStaticObject = getBuildingByUpgrade(mUpgrade, dummy, objectManager);
    }

    public int getAmountLimit() {
        return mDummy.getAmountLimit();
    }

    protected void setBuildingsAmount(int buildingsAmount) {
        mBuildingsAmount = buildingsAmount;
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
    public StaticObject getEntity() {
        return mBuildingStaticObject;
    }

    @Override
    public int getUpgrade() {
        return mUpgrade;
    }

    @Override
    public boolean buyBuilding() {
        if (getAmount() >= getAmountLimit()) {
            return false;
        }
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        if (!IPlayer.ControlType.isClientSide(player.getControlType())) {
            int cost = mDummy.getCost(mUpgrade);
            if (player.getMoney() < cost) {
                return false;
            }
            player.changeMoney(-cost);
        }
        if (mBuildingsAmount <= 0) {
            setBuildingsAmount(0);
        }
        setBuildingsAmount(mBuildingsAmount + 1);
        EventBus.getDefault().post(new BuildingsAmountChangedEvent(mPlayerName,
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

    protected static StaticObject getBuildingByUpgrade(final int upgrade,
                                                       final BuildingDummy buildingDummy,
                                                       VertexBufferObjectManager objectManager) {
        return new StaticObject(buildingDummy.getX(), buildingDummy.getY(),
                buildingDummy.getSpriteTextureRegionArray(upgrade), objectManager) {
            {
                setCost(buildingDummy.getCost(upgrade));
                setIncome((int) (getCost() * 0.03));
                setWidth(buildingDummy.getWidth());
                setHeight(buildingDummy.getWidth());
                setObjectStringId(buildingDummy.getStringId());
            }

            @Override
            protected IHealthBar createHealthBar() {
                return null;
            }
        };
    }
}
