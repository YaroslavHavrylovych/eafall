package com.gmail.yaroslavlancelot.eafall.game.events;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Provide N-seconds game cooldown before the game starts
 *
 * @author Yaroslav Havrylovych
 */
public abstract class GameStartCooldown {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    public final int COOLDOWN = 7;
    private ClientGameHud mClientGameHud;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Game cooldown constructor. You have to invoke {@link #start()} to
     * start the cooldown timer
     *
     * @param clientGameHud used to show the on HUD text
     */
    public GameStartCooldown(ClientGameHud clientGameHud) {
        mClientGameHud = clientGameHud;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================
    public void start() {
        TimerHandler timerHandler = new TimerHandler(1f, true, new ITimerCallback() {
            private int count = COOLDOWN;

            @Override
            public void onTimePassed(final TimerHandler timerHandler) {
                count--;
                if (count < 0) {
                    mClientGameHud.unregisterUpdateHandler(timerHandler);
                    mClientGameHud.hideHudText();
                    timerEnded();
                    return;
                }
                secondPassed(count);
            }
        });
        mClientGameHud.registerUpdateHandler(timerHandler);
    }

    /** triggered after the cooldown */
    public abstract void timerEnded();

    public void secondPassed(int second) {
        boolean show = true;
        int id;
        switch (second) {
            case 0:
                id = R.string.start;
                break;
            case 2:
                id = R.string.one;
                break;
            case 4:
                id = R.string.two;
                break;
            case 6:
                id = R.string.three;
                break;
            default:
                id = 0;
                show = false;
        }
        if (show) {
            mClientGameHud.showHudText(id);
        } else {
            mClientGameHud.hideHudText();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
