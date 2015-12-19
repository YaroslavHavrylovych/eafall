package com.yaroslavlancelot.eafall.game.rule;

/**
 * basic rule interface
 *
 * @author Yaroslav Havrylovych
 */
public interface IRuler {
    boolean isSuccess();

    boolean isDone();

    void startTracking();
}
