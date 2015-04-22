package com.gmail.yaroslavlancelot.eafall.game.audio;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;

import org.andengine.audio.sound.SoundManager;

/**
 * Handling sound instances
 *
 * @author Yaroslav Havrylovych
 */
public class SoundFactory {
    private static volatile SoundOperations sSoundOperations;

    public static SoundOperations init(SoundManager soundManager, Context context) {
        if (Config.getConfig().isSoundsEnabled()) {
            return sSoundOperations = new SoundOperationsImpl(soundManager, context);
        }
        return sSoundOperations = null;
    }

    public static SoundOperations getInstance() {
        return sSoundOperations;
    }
}
