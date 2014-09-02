package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

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
public class AmountDrawer {
    private static final String sAmountFontKey = "key_objects_amount_font";
    private final Text mText;
    private final RectangularShape mBackground;

    public AmountDrawer(VertexBufferObjectManager objectManager) {
        mText = new Text(0, 0,
                FontHolderUtils.getInstance().getElement(sAmountFontKey), "*", objectManager);

        mBackground = new Rectangle(0, 0, 0, 0, objectManager);
        mBackground.setColor(1, 0, 0);
        initBackground();

        mBackground.attachChild(mText);
    }

    /** change background size according to text value (new value - new size) */
    private void initBackground() {
        mText.setPosition(0, 0);
        float width = mText.getWidth() + 2 * SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_HORIZONTAL;
        float height = mText.getHeight() + 2 * SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_VERTICAL;
        width = width < height ? height : width;
        mBackground.setWidth(width);
        mBackground.setHeight(height);
        mText.setPosition(mBackground.getWidth() / 2 - mText.getWidth() / 2, SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_VERTICAL);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                SizeConstants.DESCRIPTION_POPUP_AMOUNT_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolderUtils.getInstance().addElement(sAmountFontKey, font);
    }

    /** will invoke {@code setText(String)} */
    public void setText(int value) {
        setText(Integer.toString(value));
    }

    /** set new value in amount text and redraw background */
    private void setText(String value) {
        String oldValue = mText.getText().toString();
        if (value.equalsIgnoreCase(oldValue)) {
            return;
        }
        mText.setText(value);
        initBackground();
    }

    public void draw(RectangularShape area) {
        mBackground.setX(area.getWidth() - mBackground.getWidth());
        attachTo(area);
    }

    private void attachTo(RectangularShape area) {
        area.attachChild(mBackground);
    }

    public void detach() {
        mBackground.detachSelf();
    }
}
