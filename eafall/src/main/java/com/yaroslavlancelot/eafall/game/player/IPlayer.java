package com.yaroslavlancelot.eafall.game.player;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.List;

/**
 * Player player interface. Each player can have only one player in opponents
 *
 * @author Yaroslav Havrylovych
 */
public interface IPlayer {
    int DEFAULT_CHANCE_INCOME_UNIT_DEATH = 10;

    /**
     * returns string key which used in
     * {@link com.yaroslavlancelot.eafall.game.events.SharedEvents}
     * when oxygen amount changed
     */
    String getOxygenChangedKey();

    /**
     * returns string key which used in
     * {@link com.yaroslavlancelot.eafall.game.events.SharedEvents}
     * when offence units amount changed
     */
    String getMovableUnitsAmountChangedKey();

    /** return current player units amount */
    int getUnitsAmount();

    /** return current player units amount */
    int getUnitsLimit();

    /** get player planet */
    PlanetStaticObject getPlanet();

    /**
     * set player {@link PlanetStaticObject}
     * which can be only one and if it will be destroyed then player loose
     */
    void setPlanet(PlanetStaticObject planet);

    /** get enemy player for the current player */
    IPlayer getEnemyPlayer();

    /** set enemy player */
    void setEnemyPlayer(IPlayer enemyPlayer);

    /** returns current player unit map */
    IUnitMap getUnitMap();

    /** get list of all {@link com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject} for the current player */
    List<Unit> getPlayerUnits();

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
     * Player can get money out of the dead unit if:
     * <br/>
     * Random.nextInt({@link #getUnitDeathIncomeChance()}) == 0
     *
     * @return value which contains chance to get money out of dead unit
     */
    int getUnitDeathIncomeChance();

    /**
     * create {@link IUnitMap} for the current player
     *
     * @param leftPlayer is the player on the left side of the screen
     */
    void createUnitsMap(boolean leftPlayer);

    /**
     * Obtain unit from the pool or create new one.
     * <br/>
     * unit will not be added as player object, you have to do it manually
     *
     * @param unitKey unit screen
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
     * Add new {@link com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * to a player. If this is the unit, then bonuses will be added during this method.
     *
     * @param object
     */
    void addObjectToPlayer(Unit object);

    /**
     * Remove {@link com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
     * from the player player.
     */
    void removeObjectFromPlayer(GameObject object);

    /**
     * change current player money
     *
     * @param delta value for which money will change (can be less then zero)
     */
    void changeMoney(int delta);

    /**
     * Detect whether the next income is the first one.
     * <br/>
     * Usually first income has different value than other and can be handled in different way.
     *
     * @return true if the next income is first income and false in other case
     */
    boolean isFirstIncome();

    /** player get income from all it's income objects (e.g. buildings on the planet) */
    void incomeTime();

    /** set category and maskBit for current player units */
    void changeFixtureDefFilter(short category, short maskBits);

    /**
     * triggers
     * {@link com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap}
     * positions update.
     */
    void updateUnitsPositions();

    int getBuildingsLimit();

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
        public boolean user() {
            return this == ControlType.USER_CONTROL_ON_SERVER_SIDE
                    || this == ControlType.USER_CONTROL_ON_CLIENT_SIDE;
        }

        /** returns true if type parameter is player type controlled by user (remote or local) */
        public boolean bot() {
            return this == ControlType.BOT_CONTROL_ON_SERVER_SIDE;
        }


        /** return true if type parameter corresponding to client side */
        public boolean clientSide() {
            //TODO check this when you will check the multiplayer
            return this == ControlType.REMOTE_CONTROL_ON_CLIENT_SIDE
                    || this == ControlType.USER_CONTROL_ON_CLIENT_SIDE;
        }
    }
}
