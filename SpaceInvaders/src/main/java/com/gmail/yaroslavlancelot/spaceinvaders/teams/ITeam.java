package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
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

    void setTeamPlanet(PlanetStaticObject planet);

    void removeTeamPlanet();

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

    TeamControlBehaviourType getTeamControlType();

    FixtureDef getFixtureDefUnit();
}
