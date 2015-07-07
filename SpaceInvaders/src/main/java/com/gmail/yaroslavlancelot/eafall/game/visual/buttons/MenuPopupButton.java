package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * @author Yaroslav Havrylovych
 */
//TODO load it in a SpriteBatch with other hud buttons
public class MenuPopupButton extends ButtonSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public MenuPopupButton(final VertexBufferObjectManager vertexBufferObjectManager) {
        super(SizeConstants.GAME_FIELD_WIDTH / 2,
                SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.MENU_BUTTON_HEIGHT / 2,
                (ITiledTextureRegion) TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_MENU_BUTTON),
                vertexBufferObjectManager);
        setWidth(SizeConstants.MENU_BUTTON_WIDTH);
        setHeight(SizeConstants.MENU_BUTTON_HEIGHT);
        setAlpha(.8f);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                2 * SizeConstants.MENU_BUTTON_WIDTH, SizeConstants.MENU_BUTTON_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_MENU_BUTTON, smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
