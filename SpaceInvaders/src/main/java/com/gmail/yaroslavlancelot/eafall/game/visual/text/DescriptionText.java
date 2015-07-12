package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** text used in description popup */
public class DescriptionText extends RecenterText {
    public final static int sFontSize = SizeConstants.DESCRIPTION_POPUP_TEXT_SIZE;
    public final static String sFontKey = "description_text_font_size_key";

    public DescriptionText(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public DescriptionText(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(sFontKey), text,
                text.length() < 30 ? 30 : text.length(), vertexBufferObjectManager);
        setWidth(FontUtils.measureText(FontHolder.getInstance().getElement(sFontKey), text) + 15);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 2048);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                sFontSize, true, android.graphics.Color.argb(255, 226, 255, 255));
        font.load();
        FontHolder.getInstance().addElement(sFontKey, font);
    }
}
