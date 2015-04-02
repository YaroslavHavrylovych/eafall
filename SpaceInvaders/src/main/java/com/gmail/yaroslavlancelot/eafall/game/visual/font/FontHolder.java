package com.gmail.yaroslavlancelot.eafall.game.visual.font;

import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.general.Holder;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.adt.color.Color;

/** Holds all {@link org.andengine.opengl.font.IFont} used in game */
public class FontHolder extends Holder<IFont> {
    /** current class instance (singleton realization) */
    private final static FontHolder S_FONT_HOLDER_UTILS = new FontHolder();

    private FontHolder() {
    }

    public static void loadGeneralGameFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolder.getInstance().addElement(StringConstants.KEY_FONT_MONEY, font);
        LoggerHelper.printDebugMessage(FontHolder.class.getCanonicalName(), "fonts loaded");
    }

    /** return {@link FontHolder} object */
    public static FontHolder getInstance() {
        return S_FONT_HOLDER_UTILS;
    }
}
