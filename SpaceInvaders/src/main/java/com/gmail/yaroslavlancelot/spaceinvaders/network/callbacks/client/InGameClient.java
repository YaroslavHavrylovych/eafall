package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client;

public interface InGameClient {
    void buildingCreated(int buildingId, String teamName);
    void unitCreated(String teamName, int unitId, float x, float y);
}
