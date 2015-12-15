package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.Selectable;

/**
 * General interface to use objects selection logic.
 *
 * @author Yaroslav Havrylovych
 */
public interface Selector {
    boolean isBlocked();

    void select(Selectable object);

    void deselect();

    void block();

    void unblock();

    void highlight(Selectable object);
}
