package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks;

/**
 * callback from client about game state after client connect to game but before it begins
 */
public interface PreGameStartCallbacksFromClient {
    void clientConnectionEstablished(String clientIp);
}
