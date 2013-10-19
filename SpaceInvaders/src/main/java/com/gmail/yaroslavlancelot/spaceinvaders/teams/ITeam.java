package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import org.andengine.util.color.Color;

import java.util.List;

/** Player team interface */
public interface ITeam {
    void addObjectToTeam(Unit sprite);

    void removeObjectFromTeam(Unit sprite);

    void setTeamPlanet(PlanetStaticObject planet);

    PlanetStaticObject getTeamPlanet();

    ITeam getEnemyTeam();

    void setEnemyTeam(ITeam enemyTeam);

    List<Unit> getTeamUnits();

    String getTeamName();

    int getMoney();

    void changeMoney(int delta);

    void incomeTime();

    IRace getTeamRace();

    Color getTeamColor();

    void setTeamColor(Color teamColor);
}
