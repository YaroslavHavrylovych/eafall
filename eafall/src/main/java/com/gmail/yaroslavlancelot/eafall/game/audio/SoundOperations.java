package com.gmail.yaroslavlancelot.eafall.game.audio;

import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

public interface SoundOperations {
    void setCameraHandler(ICameraHandler cameraCoordinates);

    void setMasterVolume(final float pMasterVolume);

    LimitedSoundWrapper loadSound(String path);

    void playSound(LimitedSoundWrapper sound, float x, float y);

    void playSound(LimitedSoundWrapper sound);
}
