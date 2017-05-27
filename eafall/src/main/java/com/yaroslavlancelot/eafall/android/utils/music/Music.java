package com.yaroslavlancelot.eafall.android.utils.music;

import android.support.annotation.NonNull;

/**
 * Interface to control the music. Currently played track, state, volume etc.
 */
public interface Music {
    void startPlaying(@NonNull MusicType music);

    void stopPlaying();

    enum MusicType {
        START_MENU, CAMPAIGN, IN_GAME, NONE
    }
}
