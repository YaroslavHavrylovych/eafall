package com.gmail.yaroslavlancelot.eafall.game.periodic.money;

import com.gmail.yaroslavlancelot.eafall.game.periodic.Periodic;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/** update money amount in the game */
public class MoneyUpdateCycle implements Periodic {
    public static final int MONEY_UPDATE_TIME = 10;
    private final TimerHandler mTimerHandler;

    /** money update cycle timer */
    private MoneyUpdateCycle() {
        mTimerHandler = new TimerHandler(MoneyUpdateCycle.MONEY_UPDATE_TIME, true, new Callback());
    }

    public static Periodic getPeriodic() {
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
