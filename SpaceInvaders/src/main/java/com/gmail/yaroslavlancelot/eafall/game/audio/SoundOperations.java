package com.gmail.yaroslavlancelot.eafall.game.audio;

import com.gmail.yaroslavlancelot.eafall.game.touch.ICameraHandler;

import org.andengine.audio.sound.Sound;

public interface SoundOperations {
    Sound loadSound(String path);

    void playSound(Sound sound, float x, float y);

    void setCameraHandler(ICameraHandler cameraCoordinates);

    void setMasterVolume(final float pMasterVolume);
}
