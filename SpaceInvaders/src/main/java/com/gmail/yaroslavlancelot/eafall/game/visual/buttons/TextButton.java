package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

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
public class TextButton extends ButtonSprite {
    private final static String sFontSizeKey = "game_button_phont_size_key";
    private final static int sFontSize = 45;
    private final static int padding = 30;
    private Text mText;

    public TextButton(VertexBufferObjectManager vertexBufferObjectManager, float width, float height) {
        this(vertexBufferObjectManager, width, height, 0, 0);
    }

    public TextButton(VertexBufferObjectManager vertexBufferObjectManager, float width, float height, float x, float y) {
        super(x, y,
                (ITiledTextureRegion) TextureRegionHolder.getInstance().getElement(StringConstants.FILE_GAME_BUTTON),
                vertexBufferObjectManager);
        setWidth(width);
        setHeight(height);

        mText = new Text(getWidth() / 2, getHeight() / 2,
                FontHolder.getInstance().getElement(sFontSizeKey), "", 20, vertexBufferObjectManager);
        attachChild(mText);
    }

    public void setText(String text) {
        if (text.equalsIgnoreCase(mText.getText().toString())) {
            return;
        }
        mText.setText(text);
        setWidth(mText.getWidth() + padding * 2);
        mText.setX(getWidth() / 2);
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
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                sFontSize, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontSizeKey, font);
    }
}
