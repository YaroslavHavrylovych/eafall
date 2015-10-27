package com.gmail.yaroslavlancelot.eafall.game.events.periodic.money;

import com.gmail.yaroslavlancelot.eafall.game.events.periodic.IPeriodic;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Signalize income time for each player. When income
 * triggers {@link IPlayer#incomeTime()} method.
 */
public class MoneyUpdateCycle implements IPeriodic {
    public static final int MONEY_UPDATE_TIME = 10;
    private final TimerHandler mTimerHandler;

    private MoneyUpdateCycle() {
        mTimerHandler = new TimerHandler(MoneyUpdateCycle.MONEY_UPDATE_TIME, true, new Callback());
    }

    /** return instance of the {@link IPeriodic} event which triggers income for players */
    public static IPeriodic getPeriodic() {
        return new MoneyUpdateCycle();
    }

    @Override
    public IUpdateHandler getUpdateHandler() {
        return mTimerHandler;
    }

    private static class Callback implements ITimerCallback {
        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            for (IPlayer player : PlayersHolder.getInstance().getElements()) {
                player.incomeTime();
            }
        }
    }
}
