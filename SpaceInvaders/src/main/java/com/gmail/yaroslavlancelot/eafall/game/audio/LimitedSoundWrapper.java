package com.gmail.yaroslavlancelot.eafall.game.audio;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.exception.SoundReleasedException;

/**
 * Wraps {@link Sound} to cooldown simultaneous calls (i.e. you can't trigger the same
 * {@link Sound} until cooldown passed).
 *
 * @author Yaroslav Havrylovych
 */
public class LimitedSoundWrapper {
    // ===========================================================
    // Constants
    // ===========================================================
    /** default sound cooldown */
    public static final int DEFAULT_LIMIT = 200;

    // ===========================================================
    // Fields
    // ===========================================================
    /** playable sound */
    private final Sound mSound;
    /** current cooldown */
    private final int mLimit;
    /** last sound trigger time */
    private long mStartTime;

    // ===========================================================
    // Constructors
    // ===========================================================
    public LimitedSoundWrapper(Sound sound) {
        this(sound, DEFAULT_LIMIT);
    }

    public LimitedSoundWrapper(Sound sound, int limit) {
        mSound = sound;
        mLimit = limit;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public void play() throws SoundReleasedException {
        mSound.play();
    }

    public void checkedPlay() throws SoundReleasedException {
        if (mStartTime + mLimit < System.currentTimeMillis()) {
            mStartTime = System.currentTimeMillis();
            play();
        }
    }

    public boolean isLoaded() {
        return mSound.isLoaded();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
