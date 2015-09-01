package com.gmail.yaroslavlancelot.eafall.game.client;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Provide unit creation interface
 *
 * @author Yaroslav Havrylovych
 */
public interface IUnitCreator extends IPhysicCreator {
    MovableUnit createMovableUnit(IPlayer unitPlayer, int unitKey, int x, int y, boolean isTopPath);
}
