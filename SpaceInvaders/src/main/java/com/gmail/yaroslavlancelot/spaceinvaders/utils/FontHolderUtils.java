package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import org.andengine.opengl.font.IFont;

/** Holds all {@link org.andengine.opengl.font.IFont} used in game */
public class FontHolderUtils extends HolderUtils<IFont> {
    /** current class instance (singleton realization) */
    private final static FontHolderUtils S_FONT_HOLDER_UTILS = new FontHolderUtils();

    private FontHolderUtils() {
    }

    /** return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils} object */
    public static FontHolderUtils getInstance() {
        return S_FONT_HOLDER_UTILS;
    }
}
