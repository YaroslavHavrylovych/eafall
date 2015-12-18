package com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards;

import com.yaroslavlancelot.eafall.game.client.IUnitCreator;

/**
 * As few different types of shipyards possible (depending on how much
 * ways exist) the factory will encapsulate creation process.
 *
 * @author Yaroslav Havrylovych
 */
public class ShipyardFactory {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private ShipyardFactory() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    /**
     * create shipyard instance
     *
     * @param x          planet abscissa
     * @param y          planet ordinate
     * @param playerName player name (used to check limits)
     * @param creator    used to spawn units
     * @return shipyard instance
     */
    public static IShipyard getShipyard(int x, int y, String playerName, IUnitCreator creator) {
        final IShipyard shipyard = new TwoWaysShipyard(x, y, playerName, creator);
        return shipyard;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
