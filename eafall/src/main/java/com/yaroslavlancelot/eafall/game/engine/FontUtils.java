package com.yaroslavlancelot.eafall.game.engine;

import org.andengine.opengl.font.IFont;

/**
 * Addition (basis) font utils
 *
 * @author Yaroslav Havrylovych
 */
public class FontUtils {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private FontUtils() {
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
     * Checks the width of the string with the given text and font. If measured length
     * is greater than maximum allowable, than splits the line.
     * <p/>
     * WARNING: implementation works only for max 2 rows text, so reimplement if you need more.
     *
     * @param value     text to check
     * @param font      font used to display the text
     * @param maxLength maximum allowable string length
     * @return new string split to feet
     */
    public static String splitLinesByMaxWidth(String value, IFont font, float maxLength) {
        float length = org.andengine.opengl.font.FontUtils.measureText(font, value);
        if (length >= maxLength) {
            int ind = value.lastIndexOf(' ');
            if (ind != -1) {
                value = new StringBuilder(value).replace(ind, ind + 1, "\n").toString();
            }
        }
        return value;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
