package com.gmail.yaroslavlancelot.eafall.game.audio;

import org.andengine.audio.sound.SoundManager;

/**
 * Handling sound instances
 *
 * @author Yaroslav Havrylovych
 */
public class SoundFactory {
    private static volatile SoundOperations sSoundOperations;

    public static SoundOperations getInstance() {
        return sSoundOperations;
    }

    public static SoundOperations init(SoundManager soundManager) {
        return sSoundOperations = new SoundOperationsImpl(soundManager);
    }
}
