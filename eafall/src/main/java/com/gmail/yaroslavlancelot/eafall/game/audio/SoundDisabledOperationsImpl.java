package com.gmail.yaroslavlancelot.eafall.game.audio;

import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

/**
 * Created by dima on 9/16/15.
 */
public class SoundDisabledOperationsImpl implements SoundOperations {
    @Override
    public void setCameraHandler(ICameraHandler cameraCoordinates) {
    }

    @Override
    public void setMasterVolume(float pMasterVolume) {
    }

    @Override
    public LimitedSoundWrapper loadSound(String path) {
        return null;
    }

    @Override
    public void playSound(LimitedSoundWrapper sound, float x, float y) {
    }

    @Override
    public void playSound(LimitedSoundWrapper sound) {
    }
}
