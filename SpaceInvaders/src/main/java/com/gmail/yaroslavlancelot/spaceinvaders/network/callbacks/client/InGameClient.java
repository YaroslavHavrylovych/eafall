package com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;

public interface InGameClient {
    void buildingCreated(int buildingId, String teamName);
    void unitCreated(String teamName, int unitId, float x, float y, long uniqueId);
    void unitMoved(UnitChangePositionServerMessage unitChangePositionServerMessage);
}
