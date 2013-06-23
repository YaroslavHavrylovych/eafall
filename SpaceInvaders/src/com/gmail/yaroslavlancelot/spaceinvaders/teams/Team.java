package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import org.andengine.entity.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**  */
public class Team implements ITeam {
    private List<Sprite> mTeamObjects;
    private List<ITeam> mFriendlyTeams;
    private PlanetStaticObject mTeamPlanet;

    public Team(final PlanetStaticObject teamPlanet) {
        mFriendlyTeams = new ArrayList<ITeam>();
        mTeamObjects = new ArrayList<Sprite>();
        mTeamPlanet = teamPlanet;
    }

    @SuppressWarnings("unused")
    public Team(final PlanetStaticObject teamPlanet, final ITeam iTeam) {
        this(teamPlanet);
        addFriendlyTeam(iTeam);
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
}
