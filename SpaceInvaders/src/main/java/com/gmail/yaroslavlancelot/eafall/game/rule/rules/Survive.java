package com.gmail.yaroslavlancelot.eafall.game.rule.rules;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;

/**
 * @author Yaroslav Havrylovych
 */
public class Survive extends GeneralRules {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public Survive(MissionConfig.MissionType mMissionType) {
        super(mMissionType, true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void addRules() {
    }

    @Override
    protected void onOpponentPlanetDestroyed() {
        ruleCompleted(true);
    }

    @Override
    protected void onTimeOver() {
        ruleCompleted(true);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
