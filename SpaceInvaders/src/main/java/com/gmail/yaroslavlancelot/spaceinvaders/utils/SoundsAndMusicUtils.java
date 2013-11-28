package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;

import java.io.IOException;

public class SoundsAndMusicUtils {
    private static final String TAG = SoundsAndMusicUtils.class.getCanonicalName();

    private SoundsAndMusicUtils() {
    }

    public static Music getMusic(String path, Context context, MusicManager musicManager) {
        try {
            return MusicFactory.createMusicFromAsset(musicManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate music");
        }
        return null;
    }
}
