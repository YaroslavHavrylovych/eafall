package com.gmail.yaroslavlancelot.eafall.game.team;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.List;

/**
 * Player team interface. Each team can have only one team in opponents
 *
 * @author Yaroslav Havrylovych
 */
public interface ITeam {
    /** return current team units amount */
    int getUnitsAmount();

    /**
     * get team {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be oly one
     */
    PlanetStaticObject getPlanet();

    /**
     * set team {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be only one and if it will be destroyed then team loose
     */
    void setPlanet(PlanetStaticObject planet);

    /** get enemy team for the current team */
    ITeam getEnemyTeam();

    /** set enemy team */
    void setEnemyTeam(ITeam enemyTeam);

    /** get list of all {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject} for the current team */
    List<GameObject> getTeamObjects();

    /** get current team name */
    String getName();

    /** get team money */
    int getMoney();

    /** set money value */
    void setMoney(int money);

    /** get current team alliance */
    IAlliance getAlliance();

    /** return current team color */
    Color getColor();

    /** set color for the team (used in like background color for team elements) */
    void setColor(Color teamColor);

    /**
     * return {@link ControlType} which different for all teams
     * (and even for one team in server and client)
     */
    ControlType getControlType();

    /** return {@link com.badlogic.gdx.physics.box2d.FixtureDef} which will be used for unit creation */
    FixtureDef getFixtureDefUnit();

    /** return ids of the buildings that you can build here */
    BuildingId[] getBuildingsIds();

    /**
     * Obtain unit from the pool or create new one.
     * <br/>
     * unit will not be added as team object, you have to do it manually
     *
     * @param unitKey unit id
     * @return created unit
     */
    Unit constructUnit(int unitKey);

    /**
     * create pool for each type of unit
     *
     * @param vertexManager used to create new unit if there are no units in pool
     */
    void createUnitPool(VertexBufferObjectManager vertexManager);

    /**
     * add bonus which will be applied to each unit which is added to the team
     *
     * @param teamBonus bonus to add
     */
    void addBonus(Bonus teamBonus);

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

    /** removing team plane (team defeat) */
    void removePlanet();

    /**
     * change current team money
     *
     * @param delta value for which money will change (can be less then zero)
     */
    void changeMoney(int delta);

    /** team get income from all it's income objects (e.g. buildings on the planet) */
    void incomeTime();

    /** set category and maskBit for current team units */
    void changeFixtureDefFilter(short category, short maskBits);

    /** Constant to define what team control type will be used */
    enum ControlType {
        /*
         * SERVER SIDE
         */
        /** point that team controlled by user and it's server side */
        USER_CONTROL_ON_SERVER_SIDE,
        /** point that team controlled remotely and it's server side */
        REMOTE_CONTROL_ON_SERVER_SIDE,
        /** point that team control by bot (p.s. it's always on server side) */
        BOT_CONTROL_ON_SERVER_SIDE,

        /*
         * CLIENT SIDE
         */
        /** point that team controlled by user and it's client side */
        USER_CONTROL_ON_CLIENT_SIDE,
        /** point that team controlled remotely and it's client side */
        REMOTE_CONTROL_ON_CLIENT_SIDE;

        /** returns true if type parameter is team type controlled by user (remote or local) */
        public static boolean isUserControlType(ControlType type) {
            return type == ControlType.USER_CONTROL_ON_SERVER_SIDE || type == ControlType.USER_CONTROL_ON_CLIENT_SIDE;
        }


        /** return true if type parameter corresponding to client side */
        public static boolean isClientSide(ControlType type) {
            //TODO check this when you will check the multiplayer
            return type == ControlType.REMOTE_CONTROL_ON_CLIENT_SIDE || type == ControlType.USER_CONTROL_ON_CLIENT_SIDE;
        }
    }
}
