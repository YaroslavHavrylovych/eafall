package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks;

/**
 * callback from server about game state until client didn't connect to game
 */
public interface PreGameStartCallbacksFromServer {
    void gameStart(String serverIP);

    void gameStop(String serverIP);

    void gameWaitingForPlayers(String serverIP);
}
