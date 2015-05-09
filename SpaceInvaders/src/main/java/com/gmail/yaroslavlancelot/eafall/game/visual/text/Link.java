package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used in description popup */
public class Link extends RecenterText {
    private final static String sFontSizeKey = "link_phont_size_key";
    private final static int sFontSize = SizeConstants.DESCRIPTION_POPUP_TEXT_SIZE;
    private volatile StaticHelper.OnClickListener mOnClickListener;

    public Link(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public Link(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(sFontSizeKey), text, 20, vertexBufferObjectManager);
        setTouchCallback(new LinkTouchListener());
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                sFontSize, Color.argb(255, 0, 18, 57));
        font.load();
        FontHolder.getInstance().addElement(sFontSizeKey, font);
    }

    public void setOnClickListener(StaticHelper.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void removeOnClickListener() {
        mOnClickListener = null;
    }

    private class LinkTouchListener extends StaticHelper.CustomTouchListener {
        public LinkTouchListener() {
            super(Link.this);
        }

        @Override
        public void press() {
            super.press();
            setColor(org.andengine.util.adt.color.Color.BLUE);
        }

        @Override
        public void unPress() {
            super.unPress();
            setColor(org.andengine.util.adt.color.Color.CYAN);
        }

        @Override
        public void click() {
            super.click();
            if (mOnClickListener != null) {
                mOnClickListener.onClick();
            }
        }
    }
}
