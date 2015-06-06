package com.gmail.yaroslavlancelot.eafall.game.eventbus.money;

/** hold money amount value */
public class MoneyUpdatedEvent {
    private int mMoney;
    private String mPlayerName;

    public MoneyUpdatedEvent(String playerName, int money) {
        mMoney = money;
        mPlayerName = playerName;
    }

    public int getMoney() {
        return mMoney;
    }

    public String getPlayerName() {
        return mPlayerName;
    }
}
