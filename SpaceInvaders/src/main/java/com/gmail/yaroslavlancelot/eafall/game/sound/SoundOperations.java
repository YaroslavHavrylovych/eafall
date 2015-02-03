package com.gmail.yaroslavlancelot.eafall.game.sound;

import org.andengine.audio.sound.Sound;

public interface SoundOperations {
    Sound loadSound(String path);

    void playSoundDependingFromPosition(Sound sound, float x, float y);
}
