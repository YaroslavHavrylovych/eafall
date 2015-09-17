package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

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

    SoundOperationsImpl(SoundManager soundManager) {
        mSoundManager = soundManager;
        mContext = EaFallApplication.getContext();
        setMasterVolume(EaFallApplication.getConfig().getSoundVolumeMax());
    }

    @Override
    public void setCameraHandler(ICameraHandler cameraHandler) {
        mCameraHandler = cameraHandler;
    }

    @Override
    public void setMasterVolume(float masterVolume) {
        mSoundManager.setMasterVolume(masterVolume >= 1.0f ? 0.99f : masterVolume);
    }

    @Override
    public LimitedSoundWrapper loadSound(final String path) {
        return getSound(path, mContext, mSoundManager);
    }

    @Override
    public void playSound(final LimitedSoundWrapper sound, final float x, final float y) {
        if (!(MathUtils.isInBounds(mCameraHandler.getMinX(), mCameraHandler.getMaxX(), x)
                && MathUtils.isInBounds(mCameraHandler.getMinY(), mCameraHandler.getMaxY(), y))) {
            return;
        }

        playSound(sound);
    }

    @Override
    public void playSound(LimitedSoundWrapper sound) {
        sound.checkedPlay();
    }

    public static LimitedSoundWrapper getSound(String path, Context context, SoundManager soundManager) {
        try {
            return new LimitedSoundWrapper(SoundFactory.createSoundFromAsset(soundManager, context,
                    path));
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(TAG, "can't instantiate sounds");
        }
        return null;
    }

}