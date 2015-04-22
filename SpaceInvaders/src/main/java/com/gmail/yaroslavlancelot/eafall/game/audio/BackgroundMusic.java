package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;

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
    private Music mBackgroundMusic;

    public BackgroundMusic(MusicManager musicManager, Context context) {
        mBackgroundMusic = getMusic(StringConstants.getPathToBackgroundMusic() + "background_1.ogg",
                context, musicManager);
        setMasterVolume(Config.getConfig().getMusicVolumeMax());
    }

    private Music getMusic(String path, Context context, MusicManager musicManager) {
        try {
            return MusicFactory.createMusicFromAsset(musicManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate music");
        }
        return null;
    }

    public void setMasterVolume(final float masterVolume) {
        mBackgroundMusic.setVolume(masterVolume);
    }

    public void initBackgroundMusic() {
        LoggerHelper.methodInvocation(SoundOperationsImpl.TAG, "initBackgroundMusic");
        if (mBackgroundMusic != null && !mBackgroundMusic.isPlaying()) {
            mBackgroundMusic.setLooping(true);
        }
    }

    public void playBackgroundMusic() {
        if (mBackgroundMusic != null && !mBackgroundMusic.isPlaying())
            mBackgroundMusic.resume();
    }

    public void pauseBackgroundMusic() {
        if (mBackgroundMusic != null && mBackgroundMusic.isPlaying())
            mBackgroundMusic.pause();
    }
}
