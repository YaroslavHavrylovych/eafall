package com.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.visual.buttons.sound.ButtonSpriteClickButton;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** for using in popup (generally for buildings) */
public class TextButton extends ButtonSpriteClickButton {
    private final static String sFontSizeKey = "game_button_font_size_key";
    private final static int sFontSize = 45;
    private final static int padding = 30;
    private Text mText;
    /** don't change button width with setText(String) */
    private boolean mFixedSize = false;

    public TextButton(VertexBufferObjectManager vboManager, float width, float height) {
        this(0, 0, width, height, vboManager);
    }

    public TextButton(float x, float y, float width, float height,
                      ITiledTextureRegion textureRegion, VertexBufferObjectManager vboManager) {
        super(x, y, textureRegion, vboManager);
        setWidth(width);
        setHeight(height);
        mText = new Text(getWidth() / 2, getHeight() / 2,
                FontHolder.getInstance().getElement(sFontSizeKey), "", 20, vboManager);
        attachChild(mText);
    }

    public TextButton(float x, float y, float width, float height, VertexBufferObjectManager vboManager) {
        this(x, y, width, height, (ITiledTextureRegion)
                TextureRegionHolder.getRegion(StringConstants.FILE_GAME_BUTTON), vboManager);
    }

    private boolean isFixedSize() {
        return mFixedSize;
    }

    /**
     * true: don't change button width with setText(String)
     * <p/>
     * false: redraw (change width) of the button during setText(String)
     */
    public void setFixedSize(boolean fixedSize) {
        mFixedSize = fixedSize;
    }

    public void setTextColor(org.andengine.util.adt.color.Color color) {
        mText.setColor(color);
    }

    public void setText(String text) {
        if (text.equalsIgnoreCase(mText.getText().toString())) {
            return;
        }
        mText.setText(text);
        if (!mFixedSize) {
            setWidth(mText.getWidth() + padding * 2);
            mText.setX(getWidth() / 2);
        }
    }

    public void setText(int textId) {
        setText(LocaleImpl.getInstance().getStringById(textId));
    }

    public static TextureAtlas loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager, 400, 120, TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_GAME_BUTTON, atlas, context, 0, 0, 2, 1);
        atlas.load();
        return atlas;
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),
                sFontSize, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontSizeKey, font);
    }
}
