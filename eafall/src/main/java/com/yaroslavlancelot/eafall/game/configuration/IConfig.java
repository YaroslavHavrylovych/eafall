package com.yaroslavlancelot.eafall.game.configuration;

import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

import org.andengine.util.adt.color.Color;

/**
 * Config interface you're working with in the app through
 * {@link com.yaroslavlancelot.eafall.EaFallApplication}
 *
 * @author Yaroslav Havrylovych
 */
public interface IConfig {
    int getMaxZoomFactor();

    int getMaxSimultaneousSoundStreams();

    int getUnitBuildingsLimit();

    int getWealthBuildingsLimit();

    int getDisplayWidth();

    int getDisplayHeight();

    Color getPlayerSwapColor();

    float getHudAlpha();

    ApplicationSettings getSettings();
}
