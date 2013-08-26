package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.SimpleUnit;

import java.util.List;

/**  */
public interface ITeam {
    void addObjectToTeam(SimpleUnit sprite);

    @SuppressWarnings("unused")
    void removeObjectFromTeam(SimpleUnit sprite);

    @SuppressWarnings("unused")
    boolean isObjectInTeam(SimpleUnit sprite);

    void addFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    void removeFriendlyTeam(ITeam iTeam);

    boolean isFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    List<ITeam> getFriendlyTeams();

    @SuppressWarnings("unused")
    List<SimpleUnit> getTeamUnits();

    PlanetStaticObject getTeamPlanet();

    String getTeamName();
}
