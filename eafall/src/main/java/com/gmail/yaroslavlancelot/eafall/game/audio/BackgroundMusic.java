package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;

import java.io.IOException;

/**
 * Responsible for loading and playing ingame background music.
 *
 * @author Yaroslav Havrylovych
 */
public class BackgroundMusic {
    public static final String TAG = BackgroundMusic.class.getCanonicalName();
    private Music mMusic;

    public BackgroundMusic(String path, MusicManager musicManager, Context context) {
        mMusic = getMusic(path, context, musicManager);
        setMasterVolume(EaFallApplication.getConfig().getMusicVolumeMax());
    }

    public void setMasterVolume(final float masterVolume) {
        if (mMusic != null) {
            mMusic.setVolume(masterVolume);
        }
    }

    private Music getMusic(String path, Context context, MusicManager musicManager) {
        try {
            return MusicFactory.createMusicFromAsset(musicManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate music");
        }
        return null;
    }

    public void initBackgroundMusic() {
        LoggerHelper.methodInvocation(TAG, "initBackgroundMusic");
        if (mMusic != null && !mMusic.isPlaying()) {
            mMusic.setLooping(true);
        }
    }

    public void playBackgroundMusic() {
        if (mMusic != null && !mMusic.isPlaying())
            mMusic.resume();
    }

    public void pauseBackgroundMusic() {
        if (mMusic != null && mMusic.isPlaying())
            mMusic.pause();
    }
}
