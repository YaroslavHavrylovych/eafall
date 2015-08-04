package com.gmail.yaroslavlancelot.eafall.game.popup.information;

import android.graphics.Color;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @author Yaroslav Havrylovych
 */
public class GameOverPopup extends PopupHud {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String KEY = GameOverPopup.class.getCanonicalName();
    public static final String FONT_KEY = "game_over_popup_font";

    // ===========================================================
    // Fields
    // ===========================================================
    private Text mText;

    // ===========================================================
    // Constructors
    // ===========================================================
    public GameOverPopup(final Scene scene, VertexBufferObjectManager vertexManager) {
        super(scene);
        mPopupRectangle = new Rectangle(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                1, 1, vertexManager);
        mText = new Text(0, 0, FontHolder.getInstance().getElement(FONT_KEY),
                "", 15, vertexManager);
        mPopupRectangle.attachChild(mText);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public void setSuccess(boolean success) {
        if (success) {
            mText.setText(LocaleImpl.getInstance().getStringById(R.string.win));
        } else {
            mText.setText(LocaleImpl.getInstance().getStringById(R.string.lose));
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 256);
        int sFontSize = 100;
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                sFontSize, false, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(FONT_KEY, font);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
