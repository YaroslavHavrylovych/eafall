package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;

import org.andengine.util.color.Color;

import java.util.List;

/** Player team interface. Each team can have only one team in opponents */
public interface ITeam {
    /**
     * Add new {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject}
     * to a team.
     */
    void addObjectToTeam(GameObject object);

    /**
     * Remove {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject}
     * from the team team.
     */
    void removeObjectFromTeam(GameObject object);

    /**
     * get team {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject}
     * which can be oly one
     */
    PlanetStaticObject getTeamPlanet();

    /**
     * set team {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject}
     * which can be only one and if it will be destroyed then team loose
     */
    void setTeamPlanet(PlanetStaticObject planet);

    /** removing team plane (team defeat) */
    void removeTeamPlanet();

    /** get enemy team for the current team */
    ITeam getEnemyTeam();

    /** set enemy team */
    void setEnemyTeam(ITeam enemyTeam);

    /** get list of all {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject} for the current team */
    List<GameObject> getTeamObjects();

    /** get current team name */
    String getTeamName();

    /** get team money */
    int getMoney();

    /** set money value */
    void setMoney(int money);

    /**
     * change current team money
     *
     * @param delta value for which money will change (can be less then zero)
     */
    void changeMoney(int delta);

    /** team get income from all it's income objects (e.g. buildings on the planet) */
    void incomeTime();

    /** get current team race */
    IAlliance getTeamRace();

    /** return current team color */
    Color getTeamColor();

    /** set color for the team (used in like background color for team elements) */
    void setTeamColor(Color teamColor);

    /**
     * return {@link com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType} which different for all teams
     * (and even for one team in server and client)
     */
    TeamControlBehaviourType getTeamControlType();

    /** return {@link com.badlogic.gdx.physics.box2d.FixtureDef} which will be used for unit creation */
    FixtureDef getFixtureDefUnit();

    /** set category and maskBit for current team units */
    void changeFixtureDefFilter(short category, short maskBits);

    /** return ids of the buildings that you can build here */
    BuildingId[] getBuildingsIds();
}
