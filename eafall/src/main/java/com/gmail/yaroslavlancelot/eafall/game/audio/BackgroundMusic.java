package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

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
    private boolean mMusicEnabled;

    public BackgroundMusic(String path, MusicManager musicManager, Context context) {
        mMusic = getMusic(path, context, musicManager);
        if (mMusic != null) {
            mMusic.setLooping(true);
            initSettingsCallbacks();
        }
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

    public void playBackgroundMusic() {
        if (mMusic != null && mMusicEnabled && !mMusic.isPlaying())
            mMusic.resume();
    }

    public void pauseBackgroundMusic() {
        if (mMusic != null && mMusic.isPlaying())
            mMusic.pause();
    }

    private void initSettingsCallbacks() {
        final ApplicationSettings settings
                = EaFallApplication.getConfig().getSettings();
        mMusicEnabled = settings.isMusicEnabled();
        settings.setOnConfigChangedListener(settings.KEY_PREF_MUSIC,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        mMusicEnabled = (Boolean) value;
                        if (!mMusicEnabled) {
                            pauseBackgroundMusic();
                        } else {
                            playBackgroundMusic();
                        }
                    }
                });
        settings.setOnConfigChangedListener(settings.KEY_PREF_MUSIC_VOLUME,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        setMasterVolume((Float) value);
                    }
                });
        setMasterVolume(settings.getMusicVolumeMax());
    }
}
