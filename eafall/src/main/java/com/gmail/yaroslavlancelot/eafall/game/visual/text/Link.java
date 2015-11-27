package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used in description popup */
public class Link extends RecenterText {
    public final static int FONT_SIZE = SizeConstants.DESCRIPTION_POPUP_TEXT_SIZE;
    private final static String sFontSizeKey = "link_font_size_key";
    private static int sColorUnpressed = android.graphics.Color.argb(255, 0, 18, 57);
    private static int sColorPressed = org.andengine.util.adt.color.Color.BLUE.getABGRPackedInt();
    private volatile TouchHelper.OnClickListener mOnClickListener;
    /**
     * If set then will extend each bound with contained value
     * <br/>
     * Must have 4 numbers:
     * <ol>
     * <il>top</il>
     * <il>bottom</il>
     * <il>right</il>
     * <il>left</il>
     * </ol>
     * <br/>
     */
    private float[] mTouchBoundariesExtender;

    public Link(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public Link(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(sFontSizeKey), text, 20, vertexBufferObjectManager);
        setTouchCallback(new LinkTouchListener());
    }

    public void setOnClickListener(TouchHelper.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public boolean contains(final float pX, final float pY) {
        float[] sceneCoords = convertLocalCoordinatesToSceneCoordinates(mWidth / 2, mHeight / 2);
        float hWidth = mWidth / 2;
        float hHeight = mHeight / 2;
        return mTouchBoundariesExtender == null ? super.contains(pX, pY) :
                pY < sceneCoords[1] + hHeight + mTouchBoundariesExtender[0] &&
                        pY > sceneCoords[1] - hHeight + mTouchBoundariesExtender[1] &&
                        pX < sceneCoords[0] + hWidth + mTouchBoundariesExtender[2] &&
                        pX > sceneCoords[0] - hWidth + mTouchBoundariesExtender[3];
    }

    /** Extends boundaries to detect touch event (to use in contains method) */
    public void setTouchExtender(float top, float bottom, float right, float left) {
        mTouchBoundariesExtender = new float[]{top, bottom, right, left};
    }

    /** clear custom extended touch event boundaries */
    public void removeTouchCustomBoundaries() {
        mTouchBoundariesExtender = null;
    }

    public void removeOnClickListener() {
        mOnClickListener = null;
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 256);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                FONT_SIZE, true, sColorUnpressed);
        font.load();
        FontHolder.getInstance().addElement(sFontSizeKey, font);
    }

    private class LinkTouchListener extends TouchHelper.EntityCustomTouch {
        public LinkTouchListener() {
            super(Link.this);
        }

        @Override
        public void press() {
            super.press();
            setColor(sColorPressed);
        }

        @Override
        public void unPress() {
            super.unPress();
            setColor(sColorUnpressed);
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
