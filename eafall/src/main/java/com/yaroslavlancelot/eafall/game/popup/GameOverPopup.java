package com.yaroslavlancelot.eafall.game.popup;

import android.graphics.Color;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.engine.camera.Camera;
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
 * Fully transparent and only contains the information about the winner.
 *
 * @author Yaroslav Havrylovych
 */
public class GameOverPopup extends PopupScene {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String FONT_KEY = "game_over_popup_font";

    // ===========================================================
    // Fields
    // ===========================================================
    private Text mText;

    // ===========================================================
    // Constructors
    // ===========================================================
    public GameOverPopup(Scene scene, Camera camera, VertexBufferObjectManager vertexManager) {
        super(scene, camera);
        mText = new Text(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                FontHolder.getInstance().getElement(FONT_KEY), "", 15, vertexManager);
        attachChild(mText);
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
