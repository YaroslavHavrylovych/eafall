package com.yaroslavlancelot.eafall.game.player;

import android.util.SparseArray;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnitBuilder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.SquareUnitMap;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnitBuilder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.DefenceUnitsPool;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.OffenceUnitsPool;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Player player implementation */

public class Player implements IPlayer {
    private static final String TAG = Player.class.getCanonicalName();
    /** chance to produce income to an enemy after the death of the unit */
    public final int mUnitDeathIncomeChane;
    /** fixture def of the player (used for bullet creation) */
    protected final FixtureDef mPlayerFixtureDef;
    /** player start money */
    private final int START_MONEY_VALUE;
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
    private final List<Unit> mPlayerUnits;
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
    /** amount of buildings (ids not the general amount) available to the user */
    private int mBuildingsLimit;
    /** units pools */
    private SparseArray<AfterInitializationPool<Unit>> mUnitsPools;
    /** units map to improve positioning operations performance */
    private SquareUnitMap mUnitMap;
    /** Used to update list of buildings, temporary variable, can easely be empty */
    private final List<Integer> mTmpPlayerBuildingsIds;

    public Player(final String playerName, IAlliance alliance, ControlType playerType,
                  int startMoney,
                  int unitDeathIncomeChance, int buildingsLimit, MissionConfig missionConfig) {
        mPlayerName = playerName;
        MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY = "UNIT_CREATED_" + playerName;
        OXYGEN_CHANGED_CALLBACK_KEY = "OXYGEN_CHANGED_" + playerName;
        mMaxOxygenAmount = missionConfig.getMaxOxygenAmount();
        mUnitsLimit = missionConfig.getMovableUnitsLimit();
        mPlayerUnits = new ArrayList<>(mUnitsLimit + 10);
        mAlliance = alliance;
        mBuildingsLimit = buildingsLimit;
        mTmpPlayerBuildingsIds = new ArrayList<>(mAlliance.getBuildingsAmount());
        mControlType = playerType;
        mPlayerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false);
        mUnitDeathIncomeChane = unitDeathIncomeChance;
        START_MONEY_VALUE = startMoney;
        initSettingsCallbacks();
        initBuildingsTypes(alliance);
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
    public IUnitMap getUnitMap() {
        return mUnitMap;
    }

    @Override
    public List<Unit> getPlayerUnits() {
        return mPlayerUnits;
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
    public int getUnitDeathIncomeChance() {
        return mUnitDeathIncomeChane;
    }

    @Override
    public void createUnitsMap(boolean leftPlayer) {
        mUnitMap = new SquareUnitMap(leftPlayer);
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
        synchronized (mPlayerUnits) {
            mUnitBonuses.add(playerBonus);
            for (GameObject gameObject : mPlayerUnits) {
                if (!(gameObject instanceof OffenceUnit)) {
                    continue;
                }
                OffenceUnit unit = (OffenceUnit) gameObject;
                unit.addBonus(playerBonus, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public void addObjectToPlayer(final Unit object) {
        mPlayerUnits.add(object);
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
        mPlayerUnits.remove(object);
        if (object instanceof OffenceUnit) {
            SharedEvents.valueChanged(MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY,
                    sUnitsAmount.decrementAndGet());
        }
    }

    @Override
    public void changeMoney(final int delta) {
        setMoney(mOxygenAmount + delta);
    }

    @Override
    public boolean isFirstIncome() {
        return mIsFirstIncome.get();
    }

    @Override
    public void incomeTime() {
        int value;
        if (mIsFirstIncome.getAndSet(false)) {
            value = START_MONEY_VALUE;
        } else {
            value = mPlayerPlanet.getIncome();
        }
        if (mControlType.user()) {
            ClientIncomeHandler.getIncomeHandler().makeIncome(
                    ClientIncomeHandler.IncomeType.PLANET, value);
        } else {
            changeMoney(value);
        }
    }

    @Override
    public void changeFixtureDefFilter(short category, short maskBits) {
        mPlayerFixtureDef.filter.categoryBits = category;
        mPlayerFixtureDef.filter.maskBits = maskBits;
    }

    @Override
    public void updateUnitsPositions() {
        mUnitMap.updatePositions(mPlayerUnits);
        IUnitMap enemiesUnitsMap = mEnemyPlayer.getUnitMap();
        for (int i = 0; i < mPlayerUnits.size(); i++) {
            mPlayerUnits.get(i).lifecycleTick(enemiesUnitsMap);
        }
    }

    @Override
    public int getBuildingsLimit() {
        return mBuildingsLimit;
    }

    private void initSettingsCallbacks() {
        final ApplicationSettings settings
                = EaFallApplication.getConfig().getSettings();
        settings.setOnConfigChangedListener(
                settings.KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        for (Unit unit : mPlayerUnits) {
                            unit.syncHealthBarBehaviour();
                        }
                    }
                });
    }

    private void initBuildingsTypes(IAlliance alliance) {
        SortedSet<Integer> idSet = alliance.getBuildingsIds();
        if (mBuildingsLimit == -1) {
            mBuildingsLimit = idSet.size();
        }
        mBuildingsTypesIds = new BuildingId[mBuildingsLimit];
        Iterator<Integer> it = idSet.iterator();
        int id, i = 0;
        while (it.hasNext()) {
            if (i < mBuildingsTypesIds.length) {
                id = it.next();
                mBuildingsTypesIds[i++] = BuildingId.makeId(id, 0);
            } else {
                break;
            }
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
        mTmpPlayerBuildingsIds.clear();
        mTmpPlayerBuildingsIds.addAll(mAlliance.getBuildingsIds());
        Collections.sort(mTmpPlayerBuildingsIds);
        List<Integer> allBuildings = mTmpPlayerBuildingsIds.subList(0, mBuildingsLimit);

        Iterator<Integer> it = allBuildings.iterator();
        int id;
        while (it.hasNext()) {
            id = it.next();
            //if no building on the planet, then use default one
            if (!planetBuildings.contains(id)) {
                continue;
            }
            int position = Collections.binarySearch(allBuildings, id);
            BuildingId buildingId = mBuildingsTypesIds[position];
            IBuilding building = mPlayerPlanet.getBuilding(id);
            if (buildingId.getUpgrade() == building.getUpgrade()) {
                continue;
            }
            mBuildingsTypesIds[position] = BuildingId.makeId(id, building.getUpgrade());
        }
    }
}
