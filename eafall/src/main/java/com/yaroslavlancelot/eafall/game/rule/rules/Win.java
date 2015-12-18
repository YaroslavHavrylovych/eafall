package com.yaroslavlancelot.eafall.game.rule.rules;

import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;

/**
 * You have to destroy your opponent planet until the time is over.
 * <br/>
 * If your planet destroyed you'll lose.
 * <br/>
 * If time is over you'll lose.
 *
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
