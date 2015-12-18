package com.yaroslavlancelot.eafall.game.rule;

import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.rule.rules.Collect;
import com.yaroslavlancelot.eafall.game.rule.rules.Survive;
import com.yaroslavlancelot.eafall.game.rule.rules.Win;

/**
 * Instantiate game rules based on mission data.
 * <br/>
 * Check {@link IRuler} for more information about rules
 *
 * @author Yaroslav Havrylovych
 */
public class RulesFactory {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public static IRuler createRuler(MissionConfig.MissionType type, int value, boolean timer) {
        switch (type) {
            case WIN: {
                return new Win(type, timer);
            }
            case SURVIVE: {
                return new Survive(type);
            }
            case COLLECT: {
                return new Collect(type, value, timer);
            }
        }
        throw new IllegalArgumentException("unknown rule " + type);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
