package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards;

import com.gmail.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;

import java.util.List;

/**
 * Shipyard optimized for 2-way maps (top and bottom).
 * <br/>
 * Some method will cause you to think about my incompetence and code duplication and other
 * bad things but it was made intentionally to improve performance (even in such small and
 * ugly way).
 *
 * @author Yaroslav Havrylovych
 */
public class TwoWaysShipyard extends BaseShipyard {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public TwoWaysShipyard(int x, int y, String playerName, IUnitCreator creator) {
        super(x, y, playerName, creator);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public void checkedUpdate(List<IUnitBuilding> unitBuildings) {
        updateTopPorts(unitBuildings);
        updateBottomPorts(unitBuildings);
        updateAllPorts(unitBuildings);
    }

    /**
     * update ports 0, 1 and 2. If units exist which has top path,
     * they will be assigned to any out of this ports (starting form 0)
     */
    private void updateTopPorts(List<IUnitBuilding> unitBuildings) {
        if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0) {
            return;
        }
        for (int i = 0; i < unitBuildings.size(); i++) {
            IUnitBuilding unitBuilding = unitBuildings.get(i);
            if (unitBuilding.isPaused() || !unitBuilding.isTopPath()) {
                continue;
            }
            tryWithPortPort(0, unitBuilding);
            tryWithPortPort(1, unitBuilding);
            tryWithPortPort(2, unitBuilding);
            if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0) {
                return;
            }
        }
    }

    /**
     * update ports 3 and 4. If units exist which has bottom path,
     * they will be assigned to any out of this ports (starting form 3)
     */
    private void updateBottomPorts(List<IUnitBuilding> unitBuildings) {
        if (mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
            return;
        }
        for (int i = 0; i < unitBuildings.size(); i++) {
            IUnitBuilding unitBuilding = unitBuildings.get(i);
            if (unitBuilding.isPaused() || unitBuilding.isTopPath()) {
                continue;
            }
            tryWithPortPort(3, unitBuilding);
            tryWithPortPort(4, unitBuilding);
            if (mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
                return;
            }
        }
    }

    /** updates all ports and assign any unit to any of them (just availability) */
    private void updateAllPorts(List<IUnitBuilding> unitBuildings) {
        if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0 &&
                mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
            return;
        }
        for (int i = 0; i < unitBuildings.size(); i++) {
            IUnitBuilding unitBuilding = unitBuildings.get(i);
            if (unitBuilding.isPaused()) {
                continue;
            }
            tryWithPortPort(0, unitBuilding);
            tryWithPortPort(1, unitBuilding);
            tryWithPortPort(2, unitBuilding);
            tryWithPortPort(3, unitBuilding);
            tryWithPortPort(4, unitBuilding);

            if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0 &&
                    mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
                return;
            }
        }
    }

    /**
     * if particular port available and unit buildings contains units and if limit is not
     * reached then new unit will be spawned at the port
     *
     * @param port          to spawn unit
     * @param unitBuilding unit building to spawn unit
     */
    private void tryWithPortPort(int port, IUnitBuilding unitBuilding) {
        if (mAvailablePorts.contains(port) && unitBuilding.getAvailableUnits() > 0
                && checkLimit()) {
            spawn(port, unitBuilding);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
