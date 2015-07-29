package com.gmail.yaroslavlancelot.eafall.game.player;

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
 * Player player interface. Each player can have only one player in opponents
 *
 * @author Yaroslav Havrylovych
 */
public interface IPlayer {
    /** return current player units amount */
    int getUnitsAmount();

    /**
     * get player {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be oly one
     */
    PlanetStaticObject getPlanet();

    /**
     * set player {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject}
     * which can be only one and if it will be destroyed then player loose
     */
    void setPlanet(PlanetStaticObject planet);

    /** get enemy player for the current player */
    IPlayer getEnemyPlayer();

    /** set enemy player */
    void setEnemyPlayer(IPlayer enemyPlayer);

    /** get list of all {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject} for the current player */
    List<GameObject> getPlayerObjects();

    /** get current player name */
    String getName();

    /** get player money */
    int getMoney();

    /** set money value */
    void setMoney(int money);

    /** get current player alliance */
    IAlliance getAlliance();

    /** return current player color */
    Color getColor();

    /** set color for the player (used in like background color for player elements) */
    void setColor(Color playerColor);

    /**
     * return {@link ControlType} which different for all players
     * (and even for one player in server and client)
     */
    ControlType getControlType();

    /** return {@link com.badlogic.gdx.physics.box2d.FixtureDef} which will be used for unit creation */
    FixtureDef getFixtureDefUnit();

    /** return ids of the buildings that you can build here */
    BuildingId[] getBuildingsIds();

    /**
     * Obtain unit from the pool or create new one.
     * <br/>
     * unit will not be added as player object, you have to do it manually
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
     * add bonus which will be applied to each unit which is added to the player
     *
     * @param playerBonus bonus to add
     */
    void addBonus(Bonus playerBonus);

    /**
     * Add new {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * to a player. If this is the unit, then bonuses will be added during this method.
     */
    void addObjectToPlayer(GameObject object);

    /**
     * Remove {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * from the player player.
     */
    void removeObjectFromPlayer(GameObject object);

    /** removing player plane (player defeat) */
    void removePlanet();

    /**
     * change current player money
     *
     * @param delta value for which money will change (can be less then zero)
     */
    void changeMoney(int delta);

    /** player get income from all it's income objects (e.g. buildings on the planet) */
    void incomeTime();

    /** set category and maskBit for current player units */
    void changeFixtureDefFilter(short category, short maskBits);

    /** Constant to define what player control type will be used */
    enum ControlType {
        //TODO this used in extras as String param. Need to use it as enum (enum can be passed in extras as it serialized)
        /*
         * SERVER SIDE
         */
        /** point that player controlled by user and it's server side */
        USER_CONTROL_ON_SERVER_SIDE,
        /** point that player controlled remotely and it's server side */
        REMOTE_CONTROL_ON_SERVER_SIDE,
        /** point that player control by bot (p.s. it's always on server side) */
        BOT_CONTROL_ON_SERVER_SIDE,

        /*
         * CLIENT SIDE
         */
        /** point that player controlled by user and it's client side */
        USER_CONTROL_ON_CLIENT_SIDE,
        /** point that player controlled remotely and it's client side */
        REMOTE_CONTROL_ON_CLIENT_SIDE;

        /** returns true if type parameter is player type controlled by user (remote or local) */
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
