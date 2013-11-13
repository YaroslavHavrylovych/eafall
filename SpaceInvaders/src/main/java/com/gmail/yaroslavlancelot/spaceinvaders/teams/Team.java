package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

/** Player team */
public class Team implements ITeam {
    /** current team name */
    private final String mTeamName;
    /** race of current team */
    private final IRace mTeamRace;
    /** object related to current team */
    private List<GameObject> mTeamObjects;
    /** current team main planet */
    private PlanetStaticObject mTeamPlanet;
    /** team to fight with */
    private ITeam mEnemyTeam;
    /** current team money amount */
    private int mMoneyAmount = 500;
    /** team color */
    private Color mTeamColor = new Color(100, 100, 100);

    public Team(final String teamName, IRace teamRace) {
        mTeamObjects = new ArrayList<GameObject>(20);
        mTeamName = teamName;
        mTeamRace = teamRace;
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
}
