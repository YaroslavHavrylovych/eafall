package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** for using in whole application */
public class TextButton extends ButtonSprite {
    private final static String sFontSizeKey = "game_button_phont_size_key";
    private final static int sFontSize = 50;
    private Text mText;

    public TextButton(VertexBufferObjectManager vertexBufferObjectManager, float width, float height) {
        this(vertexBufferObjectManager, width, height, 0, 0);
    }

    public TextButton(VertexBufferObjectManager vertexBufferObjectManager, float width, float height, float x, float y) {
        super(x, y,
                (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_GAME_BUTTON),
                vertexBufferObjectManager);
        setWidth(width);
        setHeight(height);

        mText = new Text(0, measureTextOrdinate(),
                FontHolderUtils.getInstance().getElement(sFontSizeKey), "", 20, vertexBufferObjectManager);
        attachChild(mText);
    }

    private float measureTextOrdinate() {
        return getHeight() / 2 - sFontSize / 2;
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 400, 100, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_GAME_BUTTON,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                sFontSize, Color.WHITE);
        font.load();
        FontHolderUtils.getInstance().addElement(sFontSizeKey, font);
    }

    public void setText(String text) {
        if (text.equalsIgnoreCase(mText.getText().toString())) {
            return;
        }
        float textWidth = FontUtils.measureText(FontHolderUtils.getInstance().getElement(sFontSizeKey), text);
        mText.setX(getWidth() / 2 - textWidth / 2);
        mText.setText(text);
    }
}
