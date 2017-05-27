package com.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ShowHudTextEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** general buildings functionality */
public abstract class Building implements IBuilding {
    /** current building player name */
    protected final String mPlayerName;
    /** current building group type */
    private final BuildingType mBuildingType;
    /** building touch callback */
    private final ITouchCallback mTouchCallback;
    /** current building dummy */
    protected BuildingDummy mDummy;
    /** amount of buildings of the current building type */
    protected int mBuildingsAmount;
    /** current building upgrade */
    protected int mUpgrade;
    /** displayed on the planet building */
    protected StaticObject mBuildingStaticObject;
    /** used to prevent click trigger if double click operation were performed */
    private TimerHandler mClickHandler;
    /** income which give all buildings of the current type (can be in percents) */
    private int mIncome;

    public Building(BuildingDummy dummy, VertexBufferObjectManager objectManager, final String playerName) {
        mBuildingType = dummy.getBuildingType();
        mPlayerName = playerName;
        mDummy = dummy;
        // init first unit building
        mBuildingStaticObject = getBuildingByUpgrade(mUpgrade, dummy, objectManager);
        //touch
        mClickHandler = new TimerHandler(
                TouchHelper.UnboundedSelectorEvents.DOUBLE_CLICK_TRIGGER_MILLIS * 1f / 1000f,
                false, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                if (PlayersHolder.getPlayer(mPlayerName).getControlType().user()) {
                    EventBus.getDefault().post(new BuildingDescriptionShowEvent(
                            BuildingId.makeId(mDummy.getBuildingId(), mUpgrade), mPlayerName));
                }
            }
        });
        mClickHandler.setTimerCallbackTriggered(true);
        mBuildingStaticObject.registerUpdateHandler(mClickHandler);
        mTouchCallback = new TouchHelper.UnboundedSelectorEvents(this) {
            @Override
            public void click() {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.SELECT);
                mClickHandler.reset();
            }

            @Override
            public void doubleClick() {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.SELECT);
                mClickHandler.setTimerCallbackTriggered(true);
                if (PlayersHolder.getPlayer(mPlayerName).getControlType().user()) {
                    onDoubleClick();
                }
            }

            @Override
            public void holdClick() {
                if (PlayersHolder.getPlayer(mPlayerName).getControlType().user()) {
                    PlayersHolder.getPlayer(mPlayerName).getPlanet().useSuppressor();
                } else {
                    EventBus.getDefault().post(new ShowHudTextEvent(R.string.wrong_planet_suppressor));
                }
            }
        };
        mBuildingStaticObject.setTouchCallback(mTouchCallback);
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
    public BuildingDummy getBuildingDummy() {
        return mDummy;
    }

    @Override
    public void setIgnoreUpdates(final boolean ignoreUpdates) {
        mBuildingStaticObject.setIgnoreUpdate(ignoreUpdates);
    }

    @Override
    public float getX() {
        return mBuildingStaticObject.getX();
    }

    @Override
    public float getY() {
        return mBuildingStaticObject.getY();
    }

    /**
     * Buying the building for the player.
     * <br/>
     * Operation can fail
     * if not enough amount of money or building limit exceed.
     * <br/>
     * Operation can be forced, in this case buying operation would
     * perform without money, and can't fail if not enough of money.
     *
     * @param force true if it's force operation (read above).
     * @return true if building bought and false in other case.
     */
    protected boolean buyBuilding(boolean force) {
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        if (getAmount() >= getAmountLimit()) {
            if (player.getControlType().user()) {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.DENIED);
            }
            return false;
        }
        if (!force && !player.getControlType().clientSide()) {
            int cost = mDummy.getCost(mUpgrade);
            if (player.getMoney() < cost) {
                SoundFactory.getInstance().playSound(GeneralSoundKeys.DENIED);
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
        if (player.getControlType().user()) {
            SoundFactory.getInstance().playSound(GeneralSoundKeys.BUTTON_CLICK);
        }
        return true;
    }

    @Override
    public boolean buyBuilding() {
        return buyBuilding(false);
    }

    @Override
    public void destroy() {
        mBuildingsAmount = 0;
        mUpgrade = 0;
        mBuildingStaticObject.detachSelf();
    }

    protected void setIncome(int income) {
        mIncome = income;
    }

    @Override
    public float getWidth() {
        return mDummy.getWidth();
    }

    @Override
    public float getHeight() {
        return mDummy.getHeight();
    }

    /** changes old building with new one */
    protected void updateRepresentation(StaticObject oldObj, StaticObject newObj) {
        oldObj.detachSelf();
        oldObj.unregisterUpdateHandler(mClickHandler);
        oldObj.removeTouchCallback();
        unregisterTouch(mBuildingStaticObject);
        newObj.registerUpdateHandler(mClickHandler);
        newObj.setTouchCallback(mTouchCallback);
        newObj.attachSelf();
        mBuildingStaticObject = newObj;
        registerTouch(mBuildingStaticObject);
    }

    public abstract void registerTouch(IEntity entity);

    public abstract void unregisterTouch(IEntity entity);

    protected void onDoubleClick() {
    }

    protected StaticObject getBuildingByUpgrade(final int upgrade,
                                                final BuildingDummy buildingDummy,
                                                VertexBufferObjectManager objectManager) {
        return new StaticObject(buildingDummy.getX(), buildingDummy.getY(),
                buildingDummy.getSpriteTextureRegionArray(upgrade), objectManager) {
            {
                PlanetStaticObject planet = PlayersHolder.getPlayer(mPlayerName).getPlanet();
                setFlippedHorizontal(planet.isFlippedHorizontal());
                setCost(buildingDummy.getCost(upgrade));
                setIncome((int) (getCost() * 0.05));
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
