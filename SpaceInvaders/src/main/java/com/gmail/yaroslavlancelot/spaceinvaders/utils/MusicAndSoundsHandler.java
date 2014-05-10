package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.ICameraCoordinates;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import java.io.IOException;

public class MusicAndSoundsHandler implements SoundOperations {
    public static final String TAG = MusicAndSoundsHandler.class.getCanonicalName();
    private SoundManager mSoundManager;
    private Context mContext;
    private ICameraCoordinates mCameraCoordinates;

    public MusicAndSoundsHandler(SoundManager soundManager, Context context) {
        mSoundManager = soundManager;
        mContext = context;
    }

    public static Music getMusic(String path, Context context, MusicManager musicManager) {
        try {
            return MusicFactory.createMusicFromAsset(musicManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate music");
        }
        return null;
    }

    public static Sound getSound(String path, Context context, SoundManager soundManager) {
        try {
            return SoundFactory.createSoundFromAsset(soundManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate sounds");
        }
        return null;
    }

    public void setCameraCoordinates(ICameraCoordinates cameraCoordinates) {
        mCameraCoordinates = cameraCoordinates;
    }

    @Override
    public Sound loadSound(final String path) {
        return getSound(path, mContext, mSoundManager);
    }

    @Override
    public void playSoundDependingFromPosition(final Sound sound, final float x, final float y) {
        if (mCameraCoordinates == null) sound.play();
        float width = mCameraCoordinates.getCameraCurrentWidth();
        float height = mCameraCoordinates.getCameraCurrentHeight();

        float soundSpreadMaxDistance = width / 2 + width / 5;
        float xDistanceVector = mCameraCoordinates.getCameraCurrentCenterX() - x;
        float xDistance = Math.abs(xDistanceVector);
        float yDistance = Math.abs(mCameraCoordinates.getCameraCurrentCenterY() - y);

        if (xDistance > soundSpreadMaxDistance || yDistance > soundSpreadMaxDistance)
            return;

        float leftVolume = 1f, rightVolume = 1f;

        if (!(xDistance > width / 2 || yDistance > height / 2)) {
            if (xDistanceVector > 0)
                rightVolume = .5f;
            else
                leftVolume = .5f;
        }

        float divider = mCameraCoordinates.getMaxZoomFactorChange() - mCameraCoordinates.getTargetZoomFactor() + 1;
        sound.setVolume(leftVolume / divider, rightVolume / divider);
        sound.play();
    }

    public class BackgroundMusic {
        private Music mBackgroundMusic;

        public BackgroundMusic(MusicManager musicManager) {
            mBackgroundMusic =
                    getMusic(GameStringsConstantsAndUtils.getPathToBackgroundMusic() + "background_1.ogg",
                            mContext, musicManager);
        }

        public void initBackgroundMusic() {
            LoggerHelper.methodInvocation(TAG, "initBackgroundMusic");
            LoggerHelper.printInformationMessage(TAG, "mBackgroundMusic != null == " + (mBackgroundMusic != null));
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
}
