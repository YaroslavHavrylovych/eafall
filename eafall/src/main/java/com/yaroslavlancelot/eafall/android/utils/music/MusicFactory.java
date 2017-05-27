package com.yaroslavlancelot.eafall.android.utils.music;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

/** Creates music instance. */
public class MusicFactory {
    private static MusicFactory sMusicFactory = new MusicFactory();
    private Music mMusic;

    public static Music getMusic() {
        return sMusicFactory.mMusic;
    }

    public static void init(Context context, ApplicationSettings settings) {
        sMusicFactory.mMusic = new MusicManagerImpl(context, settings);
    }
}
