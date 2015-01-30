package com.gmail.yaroslavlancelot.spaceinvaders.network.client.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.network.server.messages.UnitChangePositionServerMessage;

public interface InGameClient {

    void buildingCreated(BuildingId buildingId, String teamName);

    void unitCreated(String teamName, int unitId, float x, float y, long uniqueId);

    void unitMoved(UnitChangePositionServerMessage unitChangePositionServerMessage);

    void gameObjectHealthChanged(long unitUniqueId, int newUnitHealth);

    void unitFire(long gameObjectUniqueId, long attackedGameObjectUniqueId);

    void moneyChanged(String teamName, int money);

    void gameStarted();
}
