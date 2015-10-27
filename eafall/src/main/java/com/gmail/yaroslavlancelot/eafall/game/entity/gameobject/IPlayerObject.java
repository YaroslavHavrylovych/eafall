package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

/**
 * mark that particular {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
 * assigned to some player (PlayerObject is just short for PlayerGameObject)
 */
public interface IPlayerObject {
    /** return the name of the player to which this object is assigned to */
    String getPlayerName();

    /** set player name which enough to retrieve player out of the holder */
    void setPlayer(String playerName);
}
