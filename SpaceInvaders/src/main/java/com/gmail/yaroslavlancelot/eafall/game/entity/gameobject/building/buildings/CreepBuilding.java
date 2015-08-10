package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

public class CreepBuilding extends Building implements ICreepBuilding {
    private static final String TAG = CreepBuilding.class.getCanonicalName();
    /** building dummy link */
    private CreepBuildingDummy mCreepBuildingDummy;
    /** create units */
    private UnitCreatorCycle mUnitCreatorCycle;
    /** building produce units which will go by the top path */
    private boolean mIsTopPath = true;

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
        if (mUnitCreatorCycle == null) {
            mUnitCreatorCycle = new UnitCreatorCycle(mPlayerName,
                    mCreepBuildingDummy.getMovableUnitId(mUpgrade), isTopPath());
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
        VertexBufferObjectManager objectManager =
                mBuildingStaticObject.getVertexBufferObjectManager();
        mBuildingStaticObject
                = getBuildingByUpgrade(nextUpgrade, mCreepBuildingDummy, objectManager);
        if (!isFakePlanet) {
            mBuildingStaticObject.clearUpdateHandlers();
            mUnitCreatorCycle = new UnitCreatorCycle(mPlayerName,
                    mCreepBuildingDummy.getMovableUnitId(nextUpgrade),
                    mBuildingsAmount, isTopPath());
            mBuildingStaticObject.registerUpdateHandler(new TimerHandler(20, true, mUnitCreatorCycle));
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
