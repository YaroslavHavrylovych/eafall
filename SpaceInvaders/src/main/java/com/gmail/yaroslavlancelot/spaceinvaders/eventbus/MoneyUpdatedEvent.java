package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

/** hold money amount value */
public class MoneyUpdatedEvent {
    private int mMoney;
    private String mTeamName;

    public MoneyUpdatedEvent(String teamName, int money) {
        mMoney = money;
        mTeamName = teamName;
    }

    public int getMoney() {
        return mMoney;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
