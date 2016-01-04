package com.yaroslavlancelot.eafall.game.audio;

import com.yaroslavlancelot.eafall.game.touch.ICameraHandler;

public interface SoundOperations {
    void setCameraHandler(ICameraHandler cameraCoordinates);

    void setMasterVolume(final float pMasterVolume);

    LimitedSoundWrapper loadSound(String path);

    LimitedSoundWrapper loadSound(String path, int delay);

    void playSound(LimitedSoundWrapper sound, float x, float y);

    void playSound(LimitedSoundWrapper sound);

    void playSound(String key);

    void clear();
}
