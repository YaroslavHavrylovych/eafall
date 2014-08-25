package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/** used basically for display buildings number in description popup */
public class AmountDrawer implements Drawer {
    private static final String sAmountFontKey = "key_objects_amount_font";
    private final Text mText;
    private final RectangularShape mBackground;
    private RectangularShape mDrawArea;

    AmountDrawer(VertexBufferObjectManager objectManager) {
        mText = new Text(SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING,
                SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING,
                FontHolderUtils.getInstance().getElement(sAmountFontKey), "*", objectManager);

        mBackground = new Rectangle(0, 0, 0, 0, objectManager);
        initBackground();
        mBackground.setColor(1, 0, 0);

        mBackground.attachChild(mText);
    }

    /** change background size according to text value (new value - new size) */
    private void initBackground() {
        float width = mText.getWidth() + 2 * SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING;
        float height = mText.getHeight() + 2 * SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING;
        mBackground.setWidth(width);
        mBackground.setHeight(height);
    }

    static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                SizeConstants.DESCRIPTION_POPUP_AMOUNT_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(sAmountFontKey, font);
    }

    /** will invoke {@code setText(String)} */
    void setText(int value) {
        setText(Integer.toString(value));
    }

    /** set new value in amount text and redraw background */
    void setText(String value) {
        String oldValue = mText.getText().toString();
        if (value.equalsIgnoreCase(oldValue)) {
            return;
        }
        mText.setText(value);
        initBackground();
    }

    @Override
    public void draw(RectangularShape area) {
        if (mDrawArea != null) {
            mDrawArea.detachChild(mBackground);
        }
        mDrawArea = area;
        mBackground.setX(area.getWidth() - mBackground.getWidth());
        mDrawArea.attachChild(mBackground);
    }
}
