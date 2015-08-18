package com.gmail.yaroslavlancelot.eafall.game.rule.rules;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.endgame.GameEndedEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.rule.IRuler;

import de.greenrobot.event.EventBus;

/**
 * General win/loose rules. Can be extended with other rules.
 * <br/>
 * If your planet destroyed the you'll lose.
 * <br/>
 * If opponent planet destroyed then {@link GeneralRules#onOpponentPlanetDestroyed}
 * will be triggered.
 * <br/>
 * triggers {@link GeneralRules#onTimeOver()} when the game time is over
 *
 * @author Yaroslav Havrylovych
 */
public abstract class GeneralRules implements IRuler {
    // ===========================================================
    // Constants
    // ===========================================================

    protected final MissionConfig.MissionType mMissionType;
    // ===========================================================
    // Fields
    // ===========================================================
    protected boolean mDone = false;
    protected boolean mTimer = false;

    // ===========================================================
    // Constructors
    // ===========================================================
    protected GeneralRules(MissionConfig.MissionType missionType, boolean timer) {
        mMissionType = missionType;
        mTimer = timer;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean isDone() {
        return mDone;
    }

    @Override
    public void startTracking() {
        startPlanetDestroyedTracking();
        startTimerTracking();
        addRules();
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void startPlanetDestroyedTracking() {
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            player.getPlanet().addObjectDestroyedListener(new IDestroyListener() {
                @Override
                public void objectDestroyed(final BodiedSprite gameObject) {
                    if (!mDone) {
                        if (player.getControlType().user()) {
                            ruleCompleted(false);
                        } else {
                            onOpponentPlanetDestroyed();
                        }
                    }
                }
            });
        }
    }

    private void startTimerTracking() {
        if (mTimer) {
            String key = GameTime.GAME_TIME_OVER_KEY;
            SharedEvents.addCallback(new SharedEvents.DataChangedCallback(key) {
                @Override
                public void callback(final String key, final Object value) {
                    if (!mDone) {
                        onTimeOver();
                    }
                }
            });
        }
    }

    protected void ruleCompleted(boolean success) {
        mDone = true;
        EventBus.getDefault().post(new GameEndedEvent(success));
    }

    /** add rules to track */
    protected abstract void addRules();

    /** triggers after the opponent planet destroyed */
    protected abstract void onOpponentPlanetDestroyed();

    /** triggers when the game time is over (timer can be disabled for the mission/game) */
    protected abstract void onTimeOver();

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
