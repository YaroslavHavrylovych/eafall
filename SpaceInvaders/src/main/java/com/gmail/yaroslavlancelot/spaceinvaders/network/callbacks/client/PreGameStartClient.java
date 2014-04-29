package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client;

/**
 * callback from server about game state until client didn't connect to game
 */
public interface PreGameStartClient {
    void gameStart(String serverIP);

    void gameStop(String serverIP);

    void gameWaitingForPlayers(String serverIP);
}
