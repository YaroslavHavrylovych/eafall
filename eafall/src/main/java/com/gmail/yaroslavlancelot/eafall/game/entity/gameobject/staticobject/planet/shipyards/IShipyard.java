package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.ICreepBuilding;

import java.util.List;

/**
 * Shipyard used to tick updates for buildings (each second) and
 * spawn units at their positions.
 *
 * @author Yaroslav Havrylovych
 */
public interface IShipyard {
    /** invoked each second and update shipyard data (spawns units and etc) */
    void update(List<ICreepBuilding> creepBuildings);
}
