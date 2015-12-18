package com.yaroslavlancelot.eafall.network.client.callbacks;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;

public interface InGameClient {

    void buildingCreated(BuildingId buildingId, String playerName);

    void unitCreated(String playerName, int unitId, float x, float y, long uniqueId);

    void unitMoved(UnitChangePositionServerMessage unitChangePositionServerMessage);

    void gameObjectHealthChanged(long unitUniqueId, int newUnitHealth);

    void unitFire(long gameObjectUniqueId, long attackedGameObjectUniqueId);

    void moneyChanged(String playerName, int money);

    void gameStarted();
}
