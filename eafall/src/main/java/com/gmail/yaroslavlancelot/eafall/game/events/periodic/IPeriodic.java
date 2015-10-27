package com.gmail.yaroslavlancelot.eafall.game.events.periodic;

import org.andengine.engine.handler.IUpdateHandler;

/**
 * Each periodic game event has to use this interface to distinguish this events from aperiodic.
 * <br/>
 * Raw idea was to store them somewhere in one place and control though this interface.
 *
 * @author Yaroslav Havrylovych
 */
public interface IPeriodic {
    IUpdateHandler getUpdateHandler();
}
