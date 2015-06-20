package com.gmail.yaroslavlancelot.eafall.game.player;

import android.util.SparseArray;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.SharedDataCallbacks;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.MovableUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.StationaryUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.money.MoneyUpdatedEvent;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;

/** Player player implementation */
public class Player implements IPlayer {
    private static final String TAG = Player.class.getCanonicalName();
    public final int INIT_MONEY_VALUE = 200;
    /** used for {@link com.gmail.yaroslavlancelot.eafall.game.SharedDataCallbacks} */
    public final String MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY;
    /** fixture def of the player (used for bullet creation) */
    protected final FixtureDef mPlayerFixtureDef;
    /** keep track about the units amount */
    private final AtomicInteger sUnitsAmount = new AtomicInteger(0);
    /** current player name */
    private final String mPlayerName;
    /** current player alliance */
    private final IAlliance mAlliance;
    private final AtomicBoolean mIsFirstIncome = new AtomicBoolean(true);
    /** object related to current player */
    private final List<GameObject> mPlayerObjects;
    /** current player main planet */
    private volatile PlanetStaticObject mPlayerPlanet;
    /** player to fight with */
    private IPlayer mEnemyPlayer;
    /** current player money amount */
    private volatile int mMoneyAmount;
    /** player color */
    private Color mPlayerColor = new Color(100, 100, 100);
    /** player control type */
    private ControlType mControlType;
    /** array of buildings which player can build */
    private BuildingId[] mBuildingsTypesIds;
    /** bonus which will be applied to each player unit */
    private ArrayList<Bonus> mUnitBonuses = new ArrayList<Bonus>(2);
    /** units pools */
    private SparseArray<AfterInitializationPool<Unit>> mUnitsPools;

    public Player(final String playerName, IAlliance alliance, ControlType playerType) {
        mPlayerObjects = new ArrayList<GameObject>(200);
        mPlayerName = playerName;
        MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY = "UNIT_CREATED_" + playerName;
        mAlliance = alliance;
        initBuildingsTypes(alliance);
        mControlType = playerType;
        mPlayerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false);
        EventBus.getDefault().register(this);
    }

    public int getUnitsAmount() {
        return sUnitsAmount.get();
    }

    @Override
    public PlanetStaticObject getPlanet() {
        return mPlayerPlanet;
    }

    @Override
    public void setPlanet(final PlanetStaticObject planet) {
        mPlayerPlanet = planet;
    }

    @Override
    public IPlayer getEnemyPlayer() {
        return mEnemyPlayer;
    }

    @Override
    public void setEnemyPlayer(final IPlayer enemyPlayer) {
        mEnemyPlayer = enemyPlayer;
    }

    @Override
    public List<GameObject> getPlayerObjects() {
        return mPlayerObjects;
    }

    @Override
    public String getName() {
        return mPlayerName;
    }

    @Override
    public int getMoney() {
        return mMoneyAmount;
    }

    @Override
    public void setMoney(int money) {
        mMoneyAmount = money;
        EventBus.getDefault().post(new MoneyUpdatedEvent(getName(), mMoneyAmount));
    }

    @Override
    public IAlliance getAlliance() {
        return mAlliance;
    }

    @Override
    public Color getColor() {
        return mPlayerColor;
    }

    @Override
    public void setColor(final Color playerColor) {
        mPlayerColor = playerColor;
    }

    @Override
    public ControlType getControlType() {
        return mControlType;
    }

    @Override
    public FixtureDef getFixtureDefUnit() {
        return mPlayerFixtureDef;
    }

    @Override
    public BuildingId[] getBuildingsIds() {
        syncBuildingsWithPlanet();
        return mBuildingsTypesIds;
    }

    @Override
    public Unit constructUnit(final int unitKey) {
        return mUnitsPools.get(unitKey).obtainPoolItem();
    }

    @Override
    public void createUnitPool(VertexBufferObjectManager vertexManager) {
        IAlliance alliance = getAlliance();
        Set<Integer> ids = alliance.getUnitsIds();
        mUnitsPools = new SparseArray<AfterInitializationPool<Unit>>(
                ids.size());
        ITextureRegion textureRegion;
        UnitDummy dummy;
        AfterInitializationPool pool;
        UnitBuilder unitBuilder;
        for (int id : ids) {
            dummy = alliance.getUnitDummy(id);
            textureRegion = TextureRegionHolder.getRegion(dummy.getTextureRegionKey(getName()));
            unitBuilder = dummy.createBuilder(textureRegion, vertexManager);
            if (unitBuilder instanceof MovableUnitBuilder) {
                pool = new MovableUnitsPool((MovableUnitBuilder) unitBuilder, getName());
            } else if (unitBuilder instanceof StationaryUnitBuilder) {
                pool = new StationaryUnitsPool((StationaryUnitBuilder) unitBuilder, getName());
            } else {
                throw new IllegalStateException("unknown unit builder type " + unitBuilder);
            }
            mUnitsPools.put(id, pool);
        }
    }

    @Override
    public void addBonus(Bonus playerBonus) {
        synchronized (mPlayerObjects) {
            mUnitBonuses.add(playerBonus);
            for (GameObject gameObject : mPlayerObjects) {
                if (!(gameObject instanceof MovableUnit)) {
                    continue;
                }
                MovableUnit unit = (MovableUnit) gameObject;
                unit.addBonus(playerBonus, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public void addObjectToPlayer(final GameObject object) {
        LoggerHelper.printVerboseMessage(TAG, String.format("Player(%s) object added", getName()));
        synchronized (mPlayerObjects) {
            mPlayerObjects.add(object);
        }
        if (object instanceof MovableUnit) {
            for (Bonus bonus : mUnitBonuses) {
                ((MovableUnit) object).addBonus(bonus, Integer.MAX_VALUE);
            }
            SharedDataCallbacks.valueChanged(MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY,
                    sUnitsAmount.incrementAndGet());
        }
    }

    @Override
    public void removeObjectFromPlayer(final GameObject object) {
        LoggerHelper.printVerboseMessage(TAG, String.format("Player(%s) object removed", getName()));
        synchronized (mPlayerObjects) {
            mPlayerObjects.remove(object);
        }
        if (object instanceof MovableUnit) {
            SharedDataCallbacks.valueChanged(MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY,
                    sUnitsAmount.decrementAndGet());
        }
    }

    @Override
    public void removePlanet() {
        mPlayerPlanet = null;
    }

    @Override
    public void changeMoney(final int delta) {
        setMoney(mMoneyAmount + delta);
    }

    @Override
    public void incomeTime() {
        if (mPlayerPlanet == null) return;
        if (mIsFirstIncome.getAndSet(false)) {
            changeMoney(INIT_MONEY_VALUE);
            return;
        }
        changeMoney(mPlayerPlanet.getIncome());
    }

    @Override
    public void changeFixtureDefFilter(short category, short maskBits) {
        mPlayerFixtureDef.filter.categoryBits = category;
        mPlayerFixtureDef.filter.maskBits = maskBits;
    }

    private void initBuildingsTypes(IAlliance alliance) {
        Set<Integer> idSet = alliance.getBuildingsIds();
        mBuildingsTypesIds = new BuildingId[idSet.size()];
        Iterator<Integer> it = idSet.iterator();
        int id, i = 0;
        while (it.hasNext()) {
            id = it.next();
            mBuildingsTypesIds[i++] = BuildingId.makeId(id, 0);
        }
    }

    /**
     * Sync player buildings with planet buildings. So after this sync
     * {@link #mBuildingsTypesIds} will have same upgrades as on the planet.
     */
    private void syncBuildingsWithPlanet() {
        if (mPlayerPlanet.getExistingBuildingsTypesAmount() == 0) {
            return;
        }
        Set<Integer> planetBuildings = mPlayerPlanet.getExistingBuildingsTypes();
        SortedSet<Integer> allBuildings = mAlliance.getBuildingsIds();

        Iterator<Integer> it = allBuildings.iterator();
        int id;
        while (it.hasNext()) {
            id = it.next();
            //if no building on the planet, then use default one
            if (!planetBuildings.contains(id)) {
                continue;
            }
            //TODO you have to calculate position in other way
            int position = allBuildings.headSet(id).size();
            BuildingId buildingId = mBuildingsTypesIds[position];
            IBuilding building = mPlayerPlanet.getBuilding(id);
            if (buildingId.getUpgrade() == building.getUpgrade()) {
                continue;
            }
            mBuildingsTypesIds[position] = BuildingId.makeId(id, building.getUpgrade());
        }
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final UpgradeBuildingEvent upgradeBuildingEvent) {
        IPlayer player = PlayersHolder.getPlayer(upgradeBuildingEvent.getPlayerName());
        //check if its current player upgrade
        if (!player.getName().equals(getName())) {
            return;
        }
        BuildingId buildingId = upgradeBuildingEvent.getBuildingId();
        IBuilding building = getPlanet().getBuilding(buildingId.getId());
        if (building == null) {
            return;
        }
        building.upgradeBuilding();
    }
}