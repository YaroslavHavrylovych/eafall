package com.gmail.yaroslavlancelot.eafall.game.eventbus;

/** to trigger actions in update thread */
public class RunOnUpdateThreadEvent {
    private UpdateThreadRunnable mUpdateThreadRunnable;

    public RunOnUpdateThreadEvent(UpdateThreadRunnable updateThreadRunnable) {
        mUpdateThreadRunnable = updateThreadRunnable;
    }

    public UpdateThreadRunnable getCallback() {
        return mUpdateThreadRunnable;
    }

    public static interface UpdateThreadRunnable {
        void updateThreadCallback();
    }
}
