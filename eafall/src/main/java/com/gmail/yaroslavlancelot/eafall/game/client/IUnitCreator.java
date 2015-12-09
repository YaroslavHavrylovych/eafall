package com.gmail.yaroslavlancelot.eafall.game.client;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Provide unit creation interface
 *
 * @author Yaroslav Havrylovych
 */
public interface IUnitCreator extends IPhysicCreator {
    OffenceUnit createMovableUnit(IPlayer unitPlayer, int unitKey, int x, int y, boolean isTopPath);

    OffenceUnit createMovableUnit(IPlayer unitPlayer, int unitKey, int x, int y, IUnitPath unitPath);
}
