package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;
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
    //TODO we can load/unload sounds at runtime
    private boolean mSoundDisabled;

    SoundOperationsImpl(SoundManager soundManager) {
        mSoundManager = soundManager;
        mContext = EaFallApplication.getContext();
        initSettingsCallbacks();
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
        if (mSoundDisabled) {
            return;
        }

        if (!(MathUtils.isInBounds(mCameraHandler.getMinX(), mCameraHandler.getMaxX(), x)
                && MathUtils.isInBounds(mCameraHandler.getMinY(), mCameraHandler.getMaxY(), y))) {
            return;
        }

        playSound(sound);
    }

    @Override
    public void playSound(LimitedSoundWrapper sound) {
        if (mSoundDisabled) {
            return;
        }
        sound.checkedPlay();
    }

    private void initSettingsCallbacks() {
        final ApplicationSettings settings
                = EaFallApplication.getConfig().getSettings();
        boolean enable = settings.isSoundsEnabled();
        mSoundDisabled = !enable;
        settings.setOnConfigChangedListener(EaFallApplication.getConfig().getSettings().KEY_PREF_SOUNDS,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        mSoundDisabled = !((Boolean) value);
                    }
                });
        settings.setOnConfigChangedListener(EaFallApplication.getConfig().getSettings().KEY_PREF_SOUNDS_VOLUME,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        setMasterVolume((Float) value);
                    }
                });
        setMasterVolume(settings.getSoundVolumeMax());
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