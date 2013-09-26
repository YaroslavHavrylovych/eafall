package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;

import java.util.List;

/**  */
public interface ITeam {
    void addObjectToTeam(Unit sprite);

    @SuppressWarnings("unused")
    void removeObjectFromTeam(Unit sprite);

    @SuppressWarnings("unused")
    boolean isObjectInTeam(Unit sprite);

    void addFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    void removeFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    boolean isFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    List<ITeam> getFriendlyTeams();

    @SuppressWarnings("unused")
    List<Unit> getTeamUnits();

    PlanetStaticObject getTeamPlanet();

    String getTeamName();

    int getMoney();

    @SuppressWarnings("unused")
    void changeMoney(int delta);

    public void incomeTime();
}
