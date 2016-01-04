package com.yaroslavlancelot.eafall.game.audio;

import com.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.audio.sound.SoundManager;

/**
 * Handling sound instances
 *
 * @author Yaroslav Havrylovych
 */
public class SoundFactory extends SelfCleanable {
    private static volatile SoundOperations sSoundOperations;

    public static SoundOperations getInstance() {
        return sSoundOperations;
    }

    @Override
    public void clear() {
        sSoundOperations.clear();
        sSoundOperations = null;
    }

    public static SoundOperations init(SoundManager soundManager) {
        return sSoundOperations = new SoundOperationsImpl(soundManager);
    }
}
