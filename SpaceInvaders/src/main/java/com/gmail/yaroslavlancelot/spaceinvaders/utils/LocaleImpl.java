package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.Locale;

public class LocaleImpl implements Locale {
    private static volatile Locale sInstance;
    private final Context mContext;

    private LocaleImpl(final Context context) {
        mContext = context;
    }

    public synchronized static void init(final Context context) {
        sInstance = new LocaleImpl(context);
    }

    public static Locale getInstance() {
        return sInstance;
    }

    @Override
    public String getStringById(int stringId) {
        return mContext.getString(stringId);
    }
}
