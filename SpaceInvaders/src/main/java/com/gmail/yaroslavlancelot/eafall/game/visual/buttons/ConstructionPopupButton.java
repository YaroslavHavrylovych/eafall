package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** trigger buildings popup (already initialized) */
public class ConstructionPopupButton extends ButtonSprite {
    public ConstructionPopupButton(VertexBufferObjectManager vertexBufferObjectManager) {
        super(SizeConstants.GAME_FIELD_WIDTH / 2,
                SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_HEIGHT / 2,
                (ITiledTextureRegion) TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_BUILDINGS_POPUP_UP_BUTTON),
                vertexBufferObjectManager);
        setWidth(SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_WIDTH);
        setHeight(SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_HEIGHT);
        setAlpha(.8f);
        //TODO delete this if PathChooser popup which cover the whole screen doesn't exist
        EventBus.getDefault().register(this);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                2 * SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_WIDTH,
                SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_BUILDINGS_POPUP_UP_BUTTON, smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final ShowUnitPathChooser showUnitPathChooser) {
        setVisible(false);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final HideUnitPathChooser hideUnitPathChooser) {
        setVisible(true);
    }
}
