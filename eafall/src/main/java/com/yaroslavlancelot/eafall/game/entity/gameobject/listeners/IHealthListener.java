package com.yaroslavlancelot.eafall.game.entity.gameobject.listeners;

public interface IHealthListener {
    void gameObjectHealthChanged(long unitUniqueId, int newUnitHealth);
}
