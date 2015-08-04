package com.gmail.yaroslavlancelot.eafall.game.rule.rules;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;

/**
 * @author Yaroslav Havrylovych
 */
public class Win extends GeneralRules {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public Win(MissionConfig.MissionType mMissionType, boolean timer) {
        super(mMissionType, timer);
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
        ruleCompleted(false);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
