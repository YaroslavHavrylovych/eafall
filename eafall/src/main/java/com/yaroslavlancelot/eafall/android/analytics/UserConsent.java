package com.yaroslavlancelot.eafall.android.analytics;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yaroslavlancelot.eafall.EaFallApplication;

public class UserConsent {
    private static UserConsent sUserConsent = new UserConsent();
    private static final String USER_CONSENT_KEY = "key_eaf_user_consent";

    private UserConsent() {
    }

    public static UserConsent getInstance() {
        return sUserConsent;
    }

    public boolean isUserConsentAsked() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext());
        return preferences.getBoolean(USER_CONSENT_KEY, false);
    }

    public void userConsentAgreed() {
        PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext()).edit()
                .putBoolean(USER_CONSENT_KEY, true).apply();
    }
}
