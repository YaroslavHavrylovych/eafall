package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used basically for display buildings number in description popup */
public class AmountDrawer {
    private static final String sAmountFontKey = "key_objects_amount_font";
    private final static int BACKGROUND_HEIGHT = 84;
    private final Text mText;
    private final Sprite mBackgroundSprite;

    public AmountDrawer(VertexBufferObjectManager objectManager) {
        mText = new Text(0, 0,
                FontHolder.getInstance().getElement(sAmountFontKey), "*", 20, objectManager);

        mBackgroundSprite = new Sprite(0, 0, TextureRegionHolder.getRegion(
                StringConstants.FILE_AMOUNT_VALUE_BACKGROUND), objectManager);
        reposition();

        mBackgroundSprite.attachChild(mText);
    }

    /** change background size according to text value (new value - new size) */
    private void reposition() {
        mText.setPosition(0, 0);
        float width = mText.getWidth() +
                2 * SizeConstants.DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_HORIZONTAL;
        float height = BACKGROUND_HEIGHT;
        width = width < height ? height : width;
        mBackgroundSprite.setWidth(width);
        mBackgroundSprite.setHeight(height);
        mText.setPosition(mBackgroundSprite.getWidth() / 2, mBackgroundSprite.getHeight() / 2);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, 81,
                BACKGROUND_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.FILE_AMOUNT_VALUE_BACKGROUND, textureAtlas, context, 0, 0);
        textureAtlas.load();
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                SizeConstants.DESCRIPTION_POPUP_AMOUNT_FONT_SIZE, Color.WHITE);
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
        reposition();
    }

    public void draw(IEntity entity) {
        mBackgroundSprite.setPosition(
                entity.getWidth() - mBackgroundSprite.getWidth() / 2
                        + SizeConstants.DESCRIPTION_POPUP_AMOUNT_BACKGROUND_X_PADDING,
                entity.getHeight() - mBackgroundSprite.getHeight() / 2);
        attachTo(entity);
    }

    private void attachTo(IEntity entity) {
        entity.attachChild(mBackgroundSprite);
    }

    public void detach() {
        mBackgroundSprite.detachSelf();
    }
}
