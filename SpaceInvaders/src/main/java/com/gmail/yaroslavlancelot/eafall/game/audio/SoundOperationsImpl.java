package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import java.io.IOException;

/**
 * base SoundOperations implementation
 */
public class SoundOperationsImpl implements SoundOperations {
    public static final String TAG = SoundOperationsImpl.class.getCanonicalName();
    /** master volume lower then this value will not be played */
    private static final float sMinimumSoundValue = .05f;
    private SoundManager mSoundManager;
    private Context mContext;
    private ICameraHandler mCameraHandler;

    SoundOperationsImpl(SoundManager soundManager, Context context) {
        mSoundManager = soundManager;
        mContext = context;
        setMasterVolume(Config.getConfig().getSoundVolumeMax());
    }

    @Override
    public Sound loadSound(final String path) {
        return getSound(path, mContext, mSoundManager);
    }

    public static Sound getSound(String path, Context context, SoundManager soundManager) {
        try {
            return SoundFactory.createSoundFromAsset(soundManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate sounds");
        }
        return null;
    }

    @Override
    public void playSound(final Sound sound, final float x, final float y) {
        if (mCameraHandler == null) {
            throw new UnsupportedOperationException(
                    "can't use sound from position here, no camera handler initialized");
        }
        if (mSoundManager.getMasterVolume() <= sMinimumSoundValue) {
            return;
        }

        float xDistanceVector = mCameraHandler.getCenterX() - x;
        float xDistance = Math.abs(xDistanceVector);
        float yDistance = Math.abs(mCameraHandler.getCenterY() - y);

        if (xDistance > (mCameraHandler.getWidth() / 2)
                || (yDistance > mCameraHandler.getHeight() / 2)) {
            return;
        }

        float divider = mCameraHandler.getMaxZoomFactorChange() + 1
                - mCameraHandler.getZoomFactor(); //from 1 till maxZoomFactorChange
        float volume = 1.0f / divider;
        if (volume <= sMinimumSoundValue) {
            return;
        }
        sound.setVolume(volume);
        sound.play();
    }

    @Override
    public void playSound(Sound sound) {
        sound.play();
    }

    @Override
    public void setCameraHandler(ICameraHandler cameraHandler) {
        mCameraHandler = cameraHandler;
    }

    @Override
    public void setMasterVolume(float masterVolume) {
        mSoundManager.setMasterVolume(masterVolume);
    }

}