package com.gmail.yaroslavlancelot.eafall.network.client.callbacks;

/**
 * callback from server about game state until client didn't connect to game
 */
public interface PreGameStartClient {
    void gameStart(String serverIP);

    void gameStop(String serverIP);

    void gameWaitingForPlayers(String serverIP);
}
