package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Bot interface.
 * Extends runnable as the bot logic has to be handled in the separate thread.
 *
 * @author Yaroslav Havrylovych
 */
public interface IBot extends Runnable {
    /** bot has to be init before separate thread starts */
    void init(IPlayer botPlayer);
}
