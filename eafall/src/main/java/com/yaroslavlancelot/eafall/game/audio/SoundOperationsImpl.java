package com.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;
import com.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.util.math.MathUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

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
    private boolean mSoundDisabled;
    private Map<String, LimitedSoundWrapper> mSounds = new HashMap<>(10);

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
    public synchronized LimitedSoundWrapper loadSound(final String path) {
        return loadSound(path, LimitedSoundWrapper.DEFAULT_LIMIT);
    }

    @Override
    public synchronized LimitedSoundWrapper loadSound(final String path, int delay) {
        LimitedSoundWrapper sound;
        if (mSounds.containsKey(path)) {
            sound = mSounds.get(path);
        } else {
            sound = loadSound(path, mContext, delay, mSoundManager);
            mSounds.put(path, sound);
        }
        return sound;
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

    @Override
    public void playSound(final String key) {
        if (mSoundDisabled) {
            return;
        }
        mSounds.get(key).play();
    }

    @Override
    public synchronized void clear() {
        mSoundManager.releaseAll();
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

    private static LimitedSoundWrapper loadSound(String path, Context context,
                                                 int delay, SoundManager soundManager) {
        try {
            return new LimitedSoundWrapper(SoundFactory.createSoundFromAsset(soundManager, context,
                    path), delay);
        } catch (IOException e) {
            Timber.w(e, "sounds error");
        }
        return null;
    }

}