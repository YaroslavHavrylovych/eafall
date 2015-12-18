package com.yaroslavlancelot.eafall.game.player;

import com.yaroslavlancelot.eafall.general.Holder;

/** ot have access to the players from any point only by player name */
public class PlayersHolder extends Holder<IPlayer> {
    /** current class instance (singleton implementation) */
    private final static PlayersHolder sInstance = new PlayersHolder();

    private PlayersHolder() {
    }

    public static IPlayer getPlayer(String playerName) {
        return getInstance().getElement(playerName);
    }

    public static PlayersHolder getInstance() {
        return sInstance;
    }
}
