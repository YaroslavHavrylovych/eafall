package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server;

/**
 * callback from client about game state after client connect to game but before it begins
 */
public interface PreGameStartServer {
    void clientConnectionEstablished(String clientIp);
}
