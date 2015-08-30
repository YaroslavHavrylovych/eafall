package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards;

import com.gmail.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.ICreepBuilding;

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
    public void checkedUpdate(List<ICreepBuilding> creepBuildings) {
        updateTopPorts(creepBuildings);
        updateBottomPorts(creepBuildings);
        updateAllPorts(creepBuildings);
    }

    /**
     * update ports 0, 1 and 2. If units exist which has top path,
     * they will be assigned to any out of this ports (starting form 0)
     */
    private void updateTopPorts(List<ICreepBuilding> creepBuildings) {
        if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0) {
            return;
        }
        for (int i = 0; i < creepBuildings.size(); i++) {
            ICreepBuilding creepBuilding = creepBuildings.get(i);
            if (creepBuilding.isPaused() || !creepBuilding.isTopPath()) {
                continue;
            }
            tryWithPortPort(0, creepBuilding);
            tryWithPortPort(1, creepBuilding);
            tryWithPortPort(2, creepBuilding);
            if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0) {
                return;
            }
        }
    }

    /**
     * update ports 3 and 4. If units exist which has bottom path,
     * they will be assigned to any out of this ports (starting form 3)
     */
    private void updateBottomPorts(List<ICreepBuilding> creepBuildings) {
        if (mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
            return;
        }
        for (int i = 0; i < creepBuildings.size(); i++) {
            ICreepBuilding creepBuilding = creepBuildings.get(i);
            if (creepBuilding.isPaused() || creepBuilding.isTopPath()) {
                continue;
            }
            tryWithPortPort(3, creepBuilding);
            tryWithPortPort(4, creepBuilding);
            if (mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
                return;
            }
        }
    }

    /** updates all ports and assign any unit to any of them (just availability) */
    private void updateAllPorts(List<ICreepBuilding> creepBuildings) {
        if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0 &&
                mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
            return;
        }
        for (int i = 0; i < creepBuildings.size(); i++) {
            ICreepBuilding creepBuilding = creepBuildings.get(i);
            if (creepBuilding.isPaused()) {
                continue;
            }
            tryWithPortPort(0, creepBuilding);
            tryWithPortPort(1, creepBuilding);
            tryWithPortPort(2, creepBuilding);
            tryWithPortPort(3, creepBuilding);
            tryWithPortPort(4, creepBuilding);

            if (mCooldownPorts[0] > 0 && mCooldownPorts[1] > 0 && mCooldownPorts[2] > 0 &&
                    mCooldownPorts[3] > 0 && mCooldownPorts[4] > 0) {
                return;
            }
        }
    }

    /**
     * if particular port available and creep buildings contains units and if limit is not
     * reached then new unit will be spawned at the port
     *
     * @param port          to spawn unit
     * @param creepBuilding creep building to spawn unit
     */
    private void tryWithPortPort(int port, ICreepBuilding creepBuilding) {
        if (mAvailablePorts.contains(port) && creepBuilding.getAvailableUnits() > 0
                && checkLimit()) {
            spawn(port, creepBuilding);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
