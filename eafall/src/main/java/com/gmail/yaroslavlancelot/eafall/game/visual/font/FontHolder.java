package com.gmail.yaroslavlancelot.eafall.game.visual.font;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

import org.andengine.opengl.font.IFont;

/** Holds all {@link org.andengine.opengl.font.IFont} used in game */
public class FontHolder extends Holder<IFont> {
    /** current class instance (singleton realization) */
    private final static FontHolder S_FONT_HOLDER_UTILS = new FontHolder();

    private FontHolder() {
    }

    /** return {@link FontHolder} object */
    public static FontHolder getInstance() {
        return S_FONT_HOLDER_UTILS;
    }
}
