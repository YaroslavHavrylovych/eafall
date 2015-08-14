package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

public class CreepBuilding extends Building implements ICreepBuilding {
    private static final String TAG = CreepBuilding.class.getCanonicalName();
    /** if production is paused */
    protected boolean mPaused = false;
    /** building dummy link */
    private CreepBuildingDummy mCreepBuildingDummy;
    /** building produce units which will go by the top path */
    private boolean mIsTopPath = true;
    private int mCurrentTime;
    private int mCreationTime;
    private int mAvailableUnits;
    private int mUnitKey;

    public CreepBuilding(final CreepBuildingDummy dummy, VertexBufferObjectManager objectManager, String playerName) {
        super(dummy, objectManager, playerName);
        mCreepBuildingDummy = dummy;
    }

    @Override
    public boolean buyBuilding() {
        boolean result = super.buyBuilding();
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        boolean isClientSide = player.getControlType().isClientSide();
        //building was created
        if (isClientSide || result) {
            setIncome(mBuildingsAmount * mBuildingStaticObject.getIncome());
            if (isClientSide) {
                return true;
            }
        }

        //building wasn't created
        if (!result) {
            return false;
        }

        //first building created
        if (mBuildingsAmount == 1) {
            mCreationTime = mCreepBuildingDummy.getUnitCreationTime(mUpgrade);
            mUnitKey = mCreepBuildingDummy.getMovableUnitId(mUpgrade);
        }

        return true;
    }

    @Override
    public boolean isTopPath() {
        return mIsTopPath;
    }

    @Override
    public boolean isPaused() {
        return mPaused;
    }

    @Override
    public int getAvailableUnits() {
        return mAvailableUnits;
    }

    @Override
    public int getUnit() {
        mAvailableUnits--;
        return mUnitKey;
    }

    @Override
    public void setPath(boolean isTop) {
        mIsTopPath = isTop;
    }

    @Override
    public void pause() {
        mPaused = true;
    }

    @Override
    public void unPause() {
        mPaused = false;
    }

    @Override
    public void tickUpdate() {
        if (mPaused || mCreationTime > ++mCurrentTime) {
            return;
        }
        mCurrentTime = 0;
        mAvailableUnits += mBuildingsAmount;
    }

    @Override
    public boolean upgradeBuilding() {
        //check upgrade for existence and buildings amount
        int nextUpgrade = mUpgrade + 1;
        if (nextUpgrade >= mCreepBuildingDummy.getUpgrades()) {
            throw new UnsupportedOperationException("Building upgrade exceed possible building upgrades");
        }
        if (mBuildingsAmount <= 0) {
            return false;
        }

        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        boolean isFakePlanet = player.getControlType().isClientSide();
        if (!isFakePlanet) {
            int cost = mCreepBuildingDummy.getCost(nextUpgrade);
            //check money
            if (player.getMoney() < cost) {
                return false;
            }
            //upgrade
            player.changeMoney(-cost);
        }
        VertexBufferObjectManager vertexManager = mBuildingStaticObject.getVertexBufferObjectManager();
        mBuildingStaticObject = getBuildingByUpgrade(nextUpgrade, mCreepBuildingDummy, vertexManager);
        if (!isFakePlanet) {
            mBuildingStaticObject.clearUpdateHandlers();
            mCurrentTime = 0;
            mUnitKey = mCreepBuildingDummy.getMovableUnitId(nextUpgrade);
            mCreationTime = mCreepBuildingDummy.getUnitCreationTime(nextUpgrade);
        }

        IEntity oldBuilding = getEntity();
        IEntity parent = oldBuilding.getParent();
        parent.detachChild(oldBuilding);
        parent.attachChild(mBuildingStaticObject);

        mUpgrade = nextUpgrade;
        //change description popup
        EventBus.getDefault().post(new BuildingDescriptionShowEvent(
                BuildingId.makeId(mCreepBuildingDummy.getBuildingId(), nextUpgrade), mPlayerName));
        return true;
    }
}
