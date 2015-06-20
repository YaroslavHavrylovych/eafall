package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.util.math.MathUtils;

import java.io.IOException;

/**
 * base SoundOperations implementation
 *
 * @author Yaroslav Havrylovych
 */
public class SoundOperationsImpl implements SoundOperations {
    public static final String TAG = SoundOperationsImpl.class.getCanonicalName();
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

    @Override
    public void playSound(final Sound sound, final float x, final float y) {
        //TODO limit the sounds before sending it to playSound method
        if (!(MathUtils.isInBounds(mCameraHandler.getMinX(), mCameraHandler.getMaxX(), x)
                && MathUtils.isInBounds(mCameraHandler.getMinY(), mCameraHandler.getMaxY(), y))) {
            return;
        }

        playSound(sound);
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

    public static Sound getSound(String path, Context context, SoundManager soundManager) {
        try {
            return SoundFactory.createSoundFromAsset(soundManager, context, path);
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate sounds");
        }
        return null;
    }

}