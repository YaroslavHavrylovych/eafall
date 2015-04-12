package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** text used in description popup */
public class DescriptionText extends RecenterText {
    public final static int sFontSize = SizeConstants.DESCRIPTION_POPUP_TEXT_SIZE;
    private final static String sFontSizeKey = "description_text_font_size_key";

    public DescriptionText(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public DescriptionText(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(sFontSizeKey), text, text.length() < 30 ? 30 : text.length(),
                vertexBufferObjectManager);
        setWidth(FontUtils.measureText(FontHolder.getInstance().getElement(sFontSizeKey), text) + 15);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 512, 2048,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                sFontSize, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontSizeKey, font);
    }
}
