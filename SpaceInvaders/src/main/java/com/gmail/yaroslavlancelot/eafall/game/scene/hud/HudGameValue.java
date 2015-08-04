package com.gmail.yaroslavlancelot.eafall.game.scene.hud;

import android.graphics.Color;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.RecenterText;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Holder of one of the game elements. In general just a picture with description.
 * Used to display game value one hud (i.e. time, oxygen or units amount)
 *
 * @author Yaroslav Havrylovych
 */
public class HudGameValue {
    public final static String sFontKey = "hud_value_text_font_key";
    // ===========================================================
    // Constants
    // ===========================================================
    private final static String sHudMaxText = "200/200";
    // ===========================================================
    // Fields
    // ===========================================================
    private final Sprite mImage;
    private final Text mText;

    // ===========================================================
    // Constructors
    // ===========================================================
    public HudGameValue(int id, float x, String textureKey, String text, VertexBufferObjectManager vertexManager) {
        int y = SizeConstants.GAME_FIELD_HEIGHT - (id + 1) * SizeConstants.HUD_VALUES_Y_PADDING;
        ITextureRegion texture = TextureRegionHolder.getRegion(textureKey);
        mImage = new Sprite(x, y + texture.getHeight() / 2, texture, vertexManager);
        IFont font = FontHolder.getInstance().getElement(sFontKey);
        mText = new RecenterText(
                x + SizeConstants.HUD_VALUE_TEXT_OFFSET, y + font.getLineHeight() / 2,
                FontHolder.getInstance().getElement(sFontKey), sHudMaxText, vertexManager);
        mText.setText(text);
    }

    public void setText(String text) {
        mText.setText(text);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 2048);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                SizeConstants.HUD_VALUE_TEXT_FONT_SIZE,
                true, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontKey, font);
    }

    public void attachToParent(IEntity parent) {
        parent.attachChild(mImage);
        parent.attachChild(mText);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public void setImageOffset(int x, int y) {
        setOffset(mImage, x, y);
    }

    public void setTextOffset(int x, int y) {
        setOffset(mText, x, y);
    }

    private void setOffset(IEntity entity, int x, int y) {
        entity.setPosition(entity.getX() + x, entity.getY() + y);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
