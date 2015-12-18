package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.general.locale.Locale;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

/**
 * Encapsulate fare-rate logic
 *
 * @author Yaroslav Havrylovych
 */
public class FireRate {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final FireRate FIRE_RATE = new FireRate();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private FireRate() {
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
    public static FireRate getFireRate() {
        return FIRE_RATE;
    }

    /** returns fire-rate string description based on reload time value */
    public String getString(float reloadTime) {
        Locale locale = LocaleImpl.getInstance();
        if (reloadTime < 1.5) {
            return locale.getStringById(R.string.fire_rate_high);
        } else if (reloadTime < 2) {
            return locale.getStringById(R.string.fire_rate_normal);
        } else if (reloadTime < 3) {
            return locale.getStringById(R.string.fire_rate_slow);
        } else {
            return locale.getStringById(R.string.fire_rate_very_slow);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
