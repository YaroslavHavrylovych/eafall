package com.yaroslavlancelot.eafall.android.analytics;

public interface IAnalytics {
    //general
    void setStatsState(boolean enabled);

    boolean isEnabled();

    //events
    void screenViewEvent(String screenName);

    void missionStartedEvent(String missionName);

    void missionCompletedEvent(String missionName);

    void missionsFailedEvent(String missionName);

    void singleGameStartedEvent();

    void sandboxStartedEvent();

    void singlePlayerDisabled(int progress);

    //user properties

    void setUserProgress(int missionNumber);

    void musicState(boolean on);

    void soundsState(boolean on);
}
