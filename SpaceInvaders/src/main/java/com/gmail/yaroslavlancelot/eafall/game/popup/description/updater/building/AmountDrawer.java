package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building;

import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used basically for display buildings number in description popup */
public class AmountDrawer {
    private static final String sAmountFontKey = "key_objects_amount_font";
    private final Text mText;
    private final Shape mBackground;

    public AmountDrawer(VertexBufferObjectManager objectManager) {
        mText = new Text(0, 0,
                FontHolder.getInstance().getElement(sAmountFontKey), "*", 20, objectManager);

        mBackground = new Rectangle(0, 0, 0, 0, objectManager);
        mBackground.setColor(1, 0, 0);
        initBackground();

        mBackground.attachChild(mText);
    }

    /** change background size according to text value (new value - new size) */
    private void initBackground() {
        mText.setPosition(0, 0);
        float width = mText.getWidth() + 2 * Sizes.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_HORIZONTAL;
        float height = mText.getHeight() + 2 * Sizes.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_VERTICAL;
        width = width < height ? height : width;
        mBackground.setWidth(width);
        mBackground.setHeight(height);
        mText.setPosition(mBackground.getWidth() / 2, mBackground.getHeight() / 2);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                Sizes.DESCRIPTION_POPUP_AMOUNT_FONT_SIZE, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sAmountFontKey, font);
    }

    /** will invoke {@code setText(String)} */
    public void setText(int value) {
        setText(Integer.toString(value));
    }

    /** set new value in amount text and redraw background */
    public void setText(String value) {
        String oldValue = mText.getText().toString();
        if (value.equalsIgnoreCase(oldValue)) {
            return;
        }
        mText.setText(value);
        initBackground();
    }

    public void draw(Shape area) {
        mBackground.setPosition(
                area.getWidth() - mBackground.getWidth() / 2,
                area.getHeight() - mBackground.getHeight() / 2);
        attachTo(area);
    }

    private void attachTo(Shape area) {
        area.attachChild(mBackground);
    }

    public void detach() {
        mBackground.detachSelf();
    }
}
