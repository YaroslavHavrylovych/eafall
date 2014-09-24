package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text;

import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used in description popup */
public class Link extends Text {
    private final static String sFontSizeKey = "link_phont_size_key";
    private final static int sFontSize = SizeConstants.DESCRIPTION_POPUP_TEXT_SIZE;
    private volatile TouchUtils.OnClickListener mOnClickListener;

    public Link(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public Link(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolderUtils.getInstance().getElement(sFontSizeKey), text, 20, vertexBufferObjectManager);
        setTouchCallback(new LinkTouchListener());
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                sFontSize, Color.CYAN);
        font.load();
        FontHolderUtils.getInstance().addElement(sFontSizeKey, font);
    }

    public void setOnClickListener(TouchUtils.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void removeOnClickListener() {
        mOnClickListener = null;
    }

    private class LinkTouchListener extends TouchUtils.CustomTouchListener {
        public LinkTouchListener() {
            super(Link.this);
        }

        @Override
        public void press() {
            super.press();
            setColor(org.andengine.util.color.Color.BLUE);
        }

        @Override
        public void unPress() {
            super.unPress();
            setColor(org.andengine.util.color.Color.CYAN);
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
