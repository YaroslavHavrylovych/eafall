package com.gmail.yaroslavlancelot.eafall.game.configuration;

import org.andengine.util.adt.color.Color;

/**
 * @author Yaroslav Havrylovych
 */
public interface IConfig {
    int getMaxZoomFactor();

    int getMaxSimultaneousSoundStreams();

    boolean isProfilingEnabled();

    boolean isUnitsHealthBarEnabled();

    boolean isSoundsEnabled();

    float getMusicVolumeMax();

    float getSoundVolumeMax();

    boolean isMusicEnabled();

    int getCreepBuildingsLimit();

    int getWealthBuildingsLimit();

    int getDisplayWidth();

    int getDisplayHeight();

    Color getPlayerSwapColor();

    float getHudAlpha();
}
