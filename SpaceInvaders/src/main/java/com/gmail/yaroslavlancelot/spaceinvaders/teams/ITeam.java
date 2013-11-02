package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import org.andengine.util.color.Color;

import java.util.List;

/** Player team interface */
public interface ITeam {
    void addObjectToTeam(GameObject object);

    void removeObjectFromTeam(GameObject object);

    PlanetStaticObject getTeamPlanet();

    void removeTeamPlanet();

    void setTeamPlanet(PlanetStaticObject planet);

    ITeam getEnemyTeam();

    void setEnemyTeam(ITeam enemyTeam);

    List<GameObject> getTeamObjects();

    String getTeamName();

    int getMoney();

    void changeMoney(int delta);

    void incomeTime();

    IRace getTeamRace();

    Color getTeamColor();

    void setTeamColor(Color teamColor);
}
