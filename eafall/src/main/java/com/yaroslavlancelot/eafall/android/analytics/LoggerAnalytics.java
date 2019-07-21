package com.yaroslavlancelot.eafall.android.analytics;

import android.preference.PreferenceManager;

import com.yaroslavlancelot.eafall.EaFallApplication;

import timber.log.Timber;

public class LoggerAnalytics implements IAnalytics {
    private Boolean mAnalyticsDefaultState = false;
    private String mAnalyticsPrefsKey = "eafall_analytics_enabled_key";

    @Override
    public void setStatsState(boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(EaFallApplication.getContext())
                .edit().putBoolean(mAnalyticsPrefsKey, enabled).apply();
    }

    @Override
    public boolean isEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(
                EaFallApplication.getContext()).getBoolean(mAnalyticsPrefsKey,
                mAnalyticsDefaultState);
    }

    @Override
    public void screenViewEvent(String screenName) {
        Timber.i("screenViewEvent");
    }

    @Override
    public void missionStartedEvent(String missionName) {
        Timber.i("missionStartedEvent");
    }

    @Override
    public void missionCompletedEvent(String missionName) {
        Timber.i("missionCompletedEvent");
    }

    @Override
    public void missionsFailedEvent(String missionName) {
        Timber.i("missionsFailedEvent");
    }

    @Override
    public void singleGameStartedEvent() {
        Timber.i("singleGameStartedEvent");
    }

    @Override
    public void sandboxStartedEvent() {
        Timber.i("sandboxStartedEvent");
    }

    @Override
    public void singlePlayerDisabled(int progress) {
        Timber.i("singlePlayerDisabled, progress %d", progress);
    }

    @Override
    public void setUserProgress(int missionNumber) {
        Timber.i("setUserProgress %d", missionNumber);
    }

    @Override
    public void musicState(boolean on) {
        Timber.i("Music State %b", on);
    }

    @Override
    public void soundsState(boolean on) {
        Timber.i("Sound State %b", on);
    }
}
