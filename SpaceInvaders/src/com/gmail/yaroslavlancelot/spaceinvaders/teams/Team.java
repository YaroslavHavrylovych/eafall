package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import org.andengine.entity.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**  */
public class Team implements ITeam {
    private final String mTeamName;
    private List<Sprite> mTeamObjects;
    private List<ITeam> mFriendlyTeams;
    private PlanetStaticObject mTeamPlanet;

    public Team(final String teamName, final PlanetStaticObject teamPlanet) {
        mFriendlyTeams = new ArrayList<ITeam>(1);
        mTeamObjects = new ArrayList<Sprite>(20);
        mTeamPlanet = teamPlanet;
        mTeamName = teamName;
    }

    @SuppressWarnings("unused")
    public Team(final String teamName, final PlanetStaticObject teamPlanet, final ITeam iTeam) {
        this(teamName, teamPlanet);
        addFriendlyTeam(iTeam);
    }

    @Override
    public String getTeamName() {
        return mTeamName;
    }

    @Override
    public void addObjectToTeam(final Sprite sprite) {
        mTeamObjects.add(sprite);
    }

    @Override
    public void removeObjectFromTeam(final Sprite sprite) {
        mTeamObjects.remove(sprite);
    }

    @Override
    public boolean isObjectInTeam(final Sprite sprite) {
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
    public PlanetStaticObject getTeamPlanet() {
        return mTeamPlanet;
    }

    @Override
    public List<ITeam> getFriendlyTeams() {
        return mFriendlyTeams;
    }

    @Override
    public List<Sprite> getTeamUnits() {
        return mTeamObjects;
    }
}
