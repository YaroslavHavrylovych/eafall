package com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.yaroslavlancelot.eafall.game.alliance.Alliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.OffenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingSettingsPopupShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

public class UnitBuilding extends Building implements IUnitBuilding {
    /** if production is paused */
    protected boolean mPaused = false;
    /** building dummy link */
    private OffenceBuildingDummy mOffenceBuildingDummy;
    /** building produce units which will go by the top path */
    private boolean mIsTopPath = true;
    private int mCurrentTime;
    private int mCreationTime;
    private int mAvailableUnits;
    private int mUnitKey;

    public UnitBuilding(final OffenceBuildingDummy dummy, VertexBufferObjectManager objectManager, String playerName) {
        super(dummy, objectManager, playerName);
        mOffenceBuildingDummy = dummy;
    }

    @Override
    public boolean buyBuilding() {
        boolean result = super.buyBuilding();
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        boolean isClientSide = player.getControlType().clientSide();
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
            mCreationTime = mOffenceBuildingDummy.getUnitCreationTime(mUpgrade);
            mUnitKey = mOffenceBuildingDummy.getUnitId(mUpgrade);
        }

        return true;
    }

    @Override
    protected void onDoubleClick() {
        super.onDoubleClick();
        EventBus.getDefault().post(new BuildingSettingsPopupShowEvent(this));
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
        if (nextUpgrade >= mOffenceBuildingDummy.getUpgrades()) {
            throw new UnsupportedOperationException("Building upgrade exceed possible building upgrades");
        }
        if (mBuildingsAmount <= 0) {
            return false;
        }

        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        boolean isFakePlanet = player.getControlType().clientSide();
        if (!isFakePlanet) {
            int upgradeCost = Alliance.calculateBuildingUpgradeCost(mDummy.getCost(mUpgrade),
                    mDummy.getCost(nextUpgrade), mBuildingsAmount);
            //check money
            if (player.getMoney() < upgradeCost) {
                return false;
            }
            //upgrade
            player.changeMoney(-upgradeCost);
        }

        StaticObject oldBuilding = mBuildingStaticObject;
        StaticObject newBuilding = getBuildingByUpgrade(nextUpgrade, mOffenceBuildingDummy,
                oldBuilding.getVertexBufferObjectManager());
        newBuilding.setSpriteGroupName(oldBuilding.getSpriteGroupName());
        newBuilding.setPosition(oldBuilding.getX(), oldBuilding.getY());
        if (!isFakePlanet) {
            mCurrentTime = 0;
            mUnitKey = mOffenceBuildingDummy.getUnitId(nextUpgrade);
            mCreationTime = mOffenceBuildingDummy.getUnitCreationTime(nextUpgrade);
        }

        oldBuilding.detachSelf();
        newBuilding.attachSelf();

        mUpgrade = nextUpgrade;
        //change description popup
        EventBus.getDefault().post(new BuildingDescriptionShowEvent(
                BuildingId.makeId(mOffenceBuildingDummy.getBuildingId(), nextUpgrade), mPlayerName));
        return true;
    }
}
