package com.gmail.yaroslavlancelot.eafall.general.locale;

import android.content.Context;

public class LocaleImpl implements Locale {
    private static Locale sInstance;
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
