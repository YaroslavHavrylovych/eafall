package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks;

public interface PreGameStartCallback {
    void gameStart(String serverIP);
    void gameStop(String serverIP);
    void gameWaitingForPlayers(String serverIP);
}
