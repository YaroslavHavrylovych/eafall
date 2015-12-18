package com.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
    public MenuPopupButton(final VertexBufferObjectManager vboManager) {
        this(SizeConstants.GAME_FIELD_WIDTH / 2,
                SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.MENU_BUTTON_HEIGHT / 2,
                SizeConstants.MENU_BUTTON_WIDTH, SizeConstants.MENU_BUTTON_HEIGHT,
                vboManager);
    }

    public MenuPopupButton(int x, int y, int width, int height,
                           VertexBufferObjectManager vboManager) {
        super(x, y, (ITiledTextureRegion) TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_MENU_BUTTON),
                vboManager);
        setWidth(width);
        setHeight(height);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean contains(final float pX, final float pY) {
        //pX and pY parent coordinates. Extended click area
        return pX > mX - mWidth && pX < mX + mWidth && pY > mY - mHeight && pY < mY + mHeight;
    }

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
