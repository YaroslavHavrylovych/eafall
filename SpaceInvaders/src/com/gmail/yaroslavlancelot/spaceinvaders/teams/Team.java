package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;

import java.util.ArrayList;
import java.util.List;

/** Player team */
public class Team implements ITeam {
    /** current team name */
    private final String mTeamName;
    /** object related to current team */
    private List<Unit> mTeamObjects;
    /** teams friendly with current team */
    private List<ITeam> mFriendlyTeams;
    /** current team main planet */
    private PlanetStaticObject mTeamPlanet;
    /** current team money amount */
    private int mMoneyAmount;

    @SuppressWarnings("unused")
    public Team(final String teamName, final PlanetStaticObject teamPlanet, final ITeam iTeam) {
        this(teamName, teamPlanet);
        addFriendlyTeam(iTeam);
    }

    public Team(final String teamName, final PlanetStaticObject teamPlanet) {
        mFriendlyTeams = new ArrayList<ITeam>(1);
        mTeamObjects = new ArrayList<Unit>(20);
        mTeamPlanet = teamPlanet;
        mTeamName = teamName;
    }

    @Override
    public void addObjectToTeam(final Unit sprite) {
        mTeamObjects.add(sprite);
    }

    @Override
    public void removeObjectFromTeam(final Unit sprite) {
        mTeamObjects.remove(sprite);
    }

    @Override
    public boolean isObjectInTeam(final Unit sprite) {
        return mTeamObjects.contains(sprite);
    }

    @Override
    public void addFriendlyTeam(final ITeam iTeam) {
        mFriendlyTeams.add(iTeam);
    }

    @Override
    public void removeFriendlyTeam(final ITeam iTeam) {
        mFriendlyTeams.remove(iTeam);
    }

    @Override
    public boolean isFriendlyTeam(final ITeam iTeam) {
        return this.equals(iTeam) || mFriendlyTeams.contains(iTeam);
    }

    @Override
    public List<ITeam> getFriendlyTeams() {
        return mFriendlyTeams;
    }

    @Override
    public List<Unit> getTeamUnits() {
        return mTeamObjects;
    }

    @Override
    public PlanetStaticObject getTeamPlanet() {
        return mTeamPlanet;
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
        mMoneyAmount += mTeamPlanet.getObjectIncomeIncreasingValue();
    }
}
