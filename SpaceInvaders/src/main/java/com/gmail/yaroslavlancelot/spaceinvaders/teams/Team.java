package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

/** Player team implementation */
public class Team implements ITeam {
    /** fixture def of the team (used for bullet creation) */
    protected final FixtureDef mTeamFixtureDef;
    /** current team name */
    private final String mTeamName;
    /** race of current team */
    private final IRace mTeamRace;
    /** object related to current team */
    private volatile List<GameObject> mTeamObjects;
    /** current team main planet */
    private volatile PlanetStaticObject mTeamPlanet;
    /** team to fight with */
    private ITeam mEnemyTeam;
    /** current team money amount */
    private volatile int mMoneyAmount = 500;
    /** team color */
    private Color mTeamColor = new Color(100, 100, 100);
    /** team control type */
    private TeamControlBehaviourType mTeamControlBehaviourType;
    /** triggered if money changed */
    private IMoneyChangedCallback mMoneyChangedCallback;

    public Team(final String teamName, IRace teamRace, TeamControlBehaviourType teamType) {
        mTeamObjects = new ArrayList<GameObject>(20);
        mTeamName = teamName;
        mTeamRace = teamRace;
        mTeamControlBehaviourType = teamType;
        mTeamFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false);
    }

    public void setMoneyChangedCallback(IMoneyChangedCallback moneyChangedCallback) {
        mMoneyChangedCallback = moneyChangedCallback;
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
    public void changeMoney(final int delta) {
        mMoneyAmount += delta;
        if(mMoneyChangedCallback != null)
            mMoneyChangedCallback.moneyChanged(delta);
    }

    @Override
    public void incomeTime() {
        if (mTeamPlanet == null) return;
        changeMoney(mTeamPlanet.getObjectIncomeIncreasingValue());
    }

    @Override
    public IRace getTeamRace() {
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
    public void removeTeamPlanet() {
        mTeamPlanet = null;
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
    public void setMoney(int money) {
        mMoneyAmount = money;
    }

    public static interface IMoneyChangedCallback {
        void moneyChanged(int delta);
    }
}
