package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;
import android.media.MediaPlayer;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.ICameraCoordinates;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundManager;

public class MusicAndSoundsUtils implements MediaPlayer.OnPreparedListener, SoundOperations {
    private ICameraCoordinates mCameraCoordinates;
    private SoundManager mSoundManager;
    private Context mContext;

    public MusicAndSoundsUtils(ICameraCoordinates cameraCoordinates, SoundManager soundManager, Context context) {
        mCameraCoordinates = cameraCoordinates;
        mSoundManager = soundManager;
        mContext = context;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        mp.start();
    }

    @Override
    public Sound loadSound(final String path) {
        return SoundsAndMusicUtils.getSound(path, mContext, mSoundManager);
    }

    @Override
    public void playSoundDependingFromPosition(final Sound sound, final float x, final float y) {
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
}
