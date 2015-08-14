package com.gmail.yaroslavlancelot.eafall.game.rule;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.rule.rules.Collect;
import com.gmail.yaroslavlancelot.eafall.game.rule.rules.Survive;
import com.gmail.yaroslavlancelot.eafall.game.rule.rules.Win;

/**
 * Instantiate game rules based on mission data.
 * <br/>
 * After you {@link IRuler#startTracking()}/start the rul0 it'll communicate with
 * the activities with
 * {@link com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.endgame.GameEndedEvent}
 * to signalize game results
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
