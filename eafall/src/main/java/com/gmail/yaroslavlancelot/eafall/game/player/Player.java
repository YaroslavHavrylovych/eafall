package com.gmail.yaroslavlancelot.eafall.game.player;

import android.util.SparseArray;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.OffenceUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.DefenceUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.general.EbSubscribersHolder;

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

/** Player player implementation */
public class Player implements IPlayer {
    private static final String TAG = Player.class.getCanonicalName();
    public final int INIT_MONEY_VALUE = 200;
    /** fixture def of the player (used for bullet creation) */
    protected final FixtureDef mPlayerFixtureDef;
    /** used for {@link SharedEvents} */
    private final String MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY;
    /** used for {@link SharedEvents} */
    private final String OXYGEN_CHANGED_CALLBACK_KEY;
    /** keep track about the units amount */
    private final AtomicInteger sUnitsAmount = new AtomicInteger(0);
    /** current player name */
    private final String mPlayerName;
    /** current player alliance */
    private final IAlliance mAlliance;
    private final AtomicBoolean mIsFirstIncome = new AtomicBoolean(true);
    /** object related to current player */
    private final List<GameObject> mPlayerObjects;
    /** player maximum oxygen amount (can be varying depending on mission) */
    private final int mMaxOxygenAmount;
    /** player offence units limit */
    private final int mUnitsLimit;
    /** current player main planet */
    private volatile PlanetStaticObject mPlayerPlanet;
    /** player to fight with */
    private IPlayer mEnemyPlayer;
    /** current player oxygen amount/value */
    private volatile int mOxygenAmount;
    /** player color */
    private Color mPlayerColor = new Color(100, 100, 100);
    /** player control type */
    private ControlType mControlType;
    /** array of buildings which player can build */
    private BuildingId[] mBuildingsTypesIds;
    /** bonus which will be applied to each player unit */
    private ArrayList<Bonus> mUnitBonuses = new ArrayList<>(2);
    /** units pools */
    private SparseArray<AfterInitializationPool<Unit>> mUnitsPools;

    public Player(final String playerName, IAlliance alliance, ControlType playerType, MissionConfig missionConfig) {
        mPlayerName = playerName;
        MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY = "UNIT_CREATED_" + playerName;
        OXYGEN_CHANGED_CALLBACK_KEY = "OXYGEN_CHANGED_" + playerName;
        mMaxOxygenAmount = missionConfig.getMaxOxygenAmount();
        mUnitsLimit = missionConfig.getMovableUnitsLimit();
        mPlayerObjects = new ArrayList<>(mUnitsLimit + 10);
        mAlliance = alliance;
        initBuildingsTypes(alliance);
        mControlType = playerType;
        mPlayerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false);
    }

    @Override
    public String getOxygenChangedKey() {
        return OXYGEN_CHANGED_CALLBACK_KEY;
    }

    @Override
    public String getMovableUnitsAmountChangedKey() {
        return MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY;
    }

    @Override
    public int getUnitsAmount() {
        return sUnitsAmount.get();
    }

    @Override
    public int getUnitsLimit() {
        return mUnitsLimit;
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
        return mOxygenAmount;
    }

    @Override
    public void setMoney(int money) {
        mOxygenAmount = money > mMaxOxygenAmount ? mMaxOxygenAmount : money;
        SharedEvents.valueChanged(OXYGEN_CHANGED_CALLBACK_KEY, mOxygenAmount);
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
        mUnitsPools = new SparseArray<>(ids.size());
        ITextureRegion textureRegion;
        UnitDummy dummy;
        AfterInitializationPool pool;
        UnitBuilder unitBuilder;
        for (int id : ids) {
            dummy = alliance.getUnitDummy(id);
            textureRegion = TextureRegionHolder.getRegion(dummy.getTextureRegionKey(getName()));
            unitBuilder = dummy.createBuilder(textureRegion, vertexManager);
            if (unitBuilder instanceof OffenceUnitBuilder) {
                pool = new OffenceUnitsPool((OffenceUnitBuilder) unitBuilder, getName());
            } else if (unitBuilder instanceof DefenceUnitBuilder) {
                pool = new DefenceUnitsPool((DefenceUnitBuilder) unitBuilder, getName());
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
                if (!(gameObject instanceof OffenceUnit)) {
                    continue;
                }
                OffenceUnit unit = (OffenceUnit) gameObject;
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
        if (object instanceof OffenceUnit) {
            for (Bonus bonus : mUnitBonuses) {
                ((OffenceUnit) object).addBonus(bonus, Integer.MAX_VALUE);
            }
            SharedEvents.valueChanged(MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY,
                    sUnitsAmount.incrementAndGet());
        }
    }

    @Override
    public void removeObjectFromPlayer(final GameObject object) {
        LoggerHelper.printVerboseMessage(TAG, String.format("Player(%s) object removed", getName()));
        synchronized (mPlayerObjects) {
            mPlayerObjects.remove(object);
        }
        if (object instanceof OffenceUnit) {
            SharedEvents.valueChanged(MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY,
                    sUnitsAmount.decrementAndGet());
        }
    }

    @Override
    public void removePlanet() {
        mPlayerPlanet = null;
    }

    @Override
    public void changeMoney(final int delta) {
        setMoney(mOxygenAmount + delta);
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
}
