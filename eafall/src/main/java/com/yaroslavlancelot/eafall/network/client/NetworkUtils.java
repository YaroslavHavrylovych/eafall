package com.yaroslavlancelot.eafall.network.client;

import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

/**
 * @author Yaroslav Havrylovych
 */
public class NetworkUtils {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private NetworkUtils() {
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

    /**
     * Verify which side of network game you are
     *
     * @return true if you're at the client side and false in other case
     * <p/>
     * uses {@link PlayersHolder}
     */
    public static boolean isClientSide() {
        return PlayersHolder.getInstance().getElements().iterator().next()
                .getControlType().clientSide();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
