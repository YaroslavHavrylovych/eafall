package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client;

/**
 * callback from client about game state after client connect to game but before it begins
 */
public interface PreGameStart {
    void clientConnectionEstablished(String clientIp);
}
