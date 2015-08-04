package com.gmail.yaroslavlancelot.eafall.game.rule.rules;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

/**
 * You have to collect particular amount of oxygen to win the game.
 * If you destroy the opponent planet it's not the victory. You have to collect
 * this amount still.
 * <br/>
 * If your planet destroyed you'll lose.
 * <br/>
 * If time is over you'll lose.
 *
 * @author Yaroslav Havrylovych
 */
public class Collect extends GeneralRules {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** amount of oxygen to collect */
    private final int mValue;

    // ===========================================================
    // Constructors
    // ===========================================================
    public Collect(MissionConfig.MissionType mMissionType, int value, boolean timer) {
        super(mMissionType, timer);
        mValue = value;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void addRules() {
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            if (player.getControlType().isUserControlType()) {
                String key = player.getOxygenChangedKey();
                SharedEvents.addCallback(new SharedEvents.DataChangedCallback(key) {
                    @Override
                    public void callback(final String key, final Object value) {
                        int currentValue = (Integer) value;
                        if (currentValue >= mValue) {
                            ruleCompleted(true);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onOpponentPlanetDestroyed() {
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
