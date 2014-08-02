package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.color.Color;

/** Holds all {@link org.andengine.opengl.font.IFont} used in game */
public class FontHolderUtils extends HolderUtils<IFont> {
    /** current class instance (singleton realization) */
    private final static FontHolderUtils S_FONT_HOLDER_UTILS = new FontHolderUtils();

    private FontHolderUtils() {
    }

    public static void loadGameFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY, font);
        LoggerHelper.printDebugMessage(FontHolderUtils.class.getCanonicalName(), "fonts loaded");
    }

    /** return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils} object */
    public static FontHolderUtils getInstance() {
        return S_FONT_HOLDER_UTILS;
    }
}
