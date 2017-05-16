package com.yaroslavlancelot.eafall.android.utils.music;

import android.content.Context;

/** Creates music instance. */
public class MusicFactory {
    private static MusicFactory sMusicFactory = new MusicFactory();
    private Music mMusic;

    public static Music getMusic() {
        return sMusicFactory.mMusic;
    }

    public static void init(Context context) {
        sMusicFactory.mMusic = new MusicManagerImpl(context);
    }
}
