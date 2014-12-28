package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.MoneyUpdatedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.Building;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

/** Player team implementation */
public class Team implements ITeam {
    public final int INIT_MONEY_VALUE = 5000;
    /** fixture def of the team (used for bullet creation) */
    protected final FixtureDef mTeamFixtureDef;
    /** current team name */
    private final String mTeamName;
    /** race of current team */
    private final IAlliance mTeamRace;
    private final AtomicBoolean mIsFirstIncome = new AtomicBoolean(true);
    /** object related to current team */
    private volatile List<GameObject> mTeamObjects;
    /** current team main planet */
    private volatile PlanetStaticObject mTeamPlanet;
    /** team to fight with */
    private ITeam mEnemyTeam;
    /** current team money amount */
    private volatile int mMoneyAmount;
    /** team color */
    private Color mTeamColor = new Color(100, 100, 100);
    /** team control type */
    private TeamControlBehaviourType mTeamControlBehaviourType;
    /** array of buildings which team can build */
    private BuildingId[] mBuildingsTypesIds;

    public Team(final String teamName, IAlliance teamRace, TeamControlBehaviourType teamType) {
        mTeamObjects = new ArrayList<GameObject>(20);
        mTeamName = teamName;
        mTeamRace = teamRace;
        initBuildingsTypes(teamRace);
        mTeamControlBehaviourType = teamType;
        mTeamFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false);
        EventBus.getDefault().register(this);
    }

    private void initBuildingsTypes(IAlliance teamRace) {
        Set<Integer> idSet = teamRace.getBuildingsIds();
        mBuildingsTypesIds = new BuildingId[idSet.size()];
        Iterator<Integer> it = idSet.iterator();
        int id, i = 0;
        while (it.hasNext()) {
            id = it.next();
            mBuildingsTypesIds[i++] = BuildingId.makeId(id, 0);
        }
    }

    @Override
    public void addObjectToTeam(final GameObject object) {
        mTeamObjects.add(object);
    }

    @Override
    public void removeObjectFromTeam(final GameObject object) {
        mTeamObjects.remove(object);
    }

    @Override
    public PlanetStaticObject getTeamPlanet() {
        return mTeamPlanet;
    }

    @Override
    public void setTeamPlanet(final PlanetStaticObject planet) {
        mTeamPlanet = planet;
    }

    @Override
    public void removeTeamPlanet() {
        mTeamPlanet = null;
    }

    @Override
    public ITeam getEnemyTeam() {
        return mEnemyTeam;
    }

    @Override
    public void setEnemyTeam(final ITeam enemyTeam) {
        mEnemyTeam = enemyTeam;
    }

    @Override
    public List<GameObject> getTeamObjects() {
        return mTeamObjects;
    }

    @Override
    public String getTeamName() {
        return mTeamName;
    }

    @Override
    public int getMoney() {
        return mMoneyAmount;
    }

    @Override
    public void setMoney(int money) {
        mMoneyAmount = money;
        EventBus.getDefault().post(new MoneyUpdatedEvent(getTeamName(), mMoneyAmount));
    }

    @Override
    public void changeMoney(final int delta) {
        setMoney(mMoneyAmount + delta);
    }

    @Override
    public void incomeTime() {
        if (mTeamPlanet == null) return;
        if (mIsFirstIncome.getAndSet(false)) {
            changeMoney(INIT_MONEY_VALUE);
            return;
        }
        changeMoney(mTeamPlanet.getIncome());
    }

    @Override
    public IAlliance getTeamRace() {
        return mTeamRace;
    }

    @Override
    public Color getTeamColor() {
        return mTeamColor;
    }

    @Override
    public void setTeamColor(final Color teamColor) {
        mTeamColor = teamColor;
    }

    @Override
    public TeamControlBehaviourType getTeamControlType() {
        return mTeamControlBehaviourType;
    }

    @Override
    public FixtureDef getFixtureDefUnit() {
        return mTeamFixtureDef;
    }

    @Override
    public void changeFixtureDefFilter(short category, short maskBits) {
        mTeamFixtureDef.filter.categoryBits = category;
        mTeamFixtureDef.filter.maskBits = maskBits;
    }

    @Override
    public BuildingId[] getBuildingsIds() {
        syncBuildingsWithPlanet();
        return mBuildingsTypesIds;
    }

    private void syncBuildingsWithPlanet() {
        if (mTeamPlanet.getExistingBuildingsTypesAmount() == 0) {
            return;
        }
        Set<Integer> planetBuildings = mTeamPlanet.getExistingBuildingsTypes();
        SortedSet<Integer> allBuildings = mTeamRace.getBuildingsIds();

        Iterator<Integer> it = allBuildings.iterator();
        int id;
        while (it.hasNext()) {
            id = it.next();
            //if no building on the planet, then use default one
            if (!planetBuildings.contains(id)) {
                continue;
            }
            int position = allBuildings.headSet(id).size();
            BuildingId buildingId = mBuildingsTypesIds[position];
            Building building = mTeamPlanet.getBuilding(id);
            if (buildingId.getUpgrade() == building.getUpgrade()) {
                continue;
            }
            mBuildingsTypesIds[position] = BuildingId.makeId(id, building.getUpgrade());
        }
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final UpgradeBuildingEvent upgradeBuildingEvent) {
        ITeam team = TeamsHolder.getTeam(upgradeBuildingEvent.getTeamName());
        //check if its current team upgrade
        if (!team.getTeamName().equals(getTeamName())) {
            return;
        }
        BuildingId buildingId = upgradeBuildingEvent.getBuildingId();
        Building building = getTeamPlanet().getBuilding(buildingId.getId());
        if (building == null) {
            return;
        }
        building.upgradeBuilding();
    }
}
