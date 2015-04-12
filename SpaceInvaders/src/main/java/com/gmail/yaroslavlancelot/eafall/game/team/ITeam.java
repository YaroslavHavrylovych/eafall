package com.gmail.yaroslavlancelot.eafall.game.team;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;

import org.andengine.util.adt.color.Color;

import java.util.List;

/** Player team interface. Each team can have only one team in opponents */
public interface ITeam {
    /**
     * add bonus which will be applied to each unit which is added to the team
     *
     * @param teamBonus bonus to add
     */
    void addTeamBonus(Bonus teamBonus);

    /**
     * Add new {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * to a team. If this is the unit, then bonuses will be added during this method.
     */
    void addObjectToTeam(GameObject object);

    /**
     * Remove {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * from the team team.
     */
    void removeObjectFromTeam(GameObject object);

    /**
     * get team {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be oly one
     */
    PlanetStaticObject getTeamPlanet();

    /**
     * set team {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be only one and if it will be destroyed then team loose
     */
    void setTeamPlanet(PlanetStaticObject planet);

    /** removing team plane (team defeat) */
    void removeTeamPlanet();

    /** get enemy team for the current team */
    ITeam getEnemyTeam();

    /** set enemy team */
    void setEnemyTeam(ITeam enemyTeam);

    /** get list of all {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject} for the current team */
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
     * return {@link TeamControlBehaviourType} which different for all teams
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
