package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

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
        super(Sizes.GAME_FIELD_WIDTH / 2 - Sizes.BUILDING_POPUP_INVOCATION_BUTTON_SIZE / 2,
                Sizes.GAME_FIELD_HEIGHT - Sizes.BUILDING_POPUP_INVOCATION_BUTTON_SIZE,
                (ITiledTextureRegion) TextureRegionHolder.getInstance().getElement(StringsAndPath.FILE_BUILDINGS_POPUP_UP_BUTTON),
                vertexBufferObjectManager);
        int size = Sizes.BUILDING_POPUP_INVOCATION_BUTTON_SIZE;
        setWidth(size);
        setHeight(size);
        setAlpha(.6f);
        EventBus.getDefault().register(this);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        int size = Sizes.BUILDING_POPUP_INVOCATION_BUTTON_SIZE;
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 2 * size, size, TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringsAndPath.FILE_BUILDINGS_POPUP_UP_BUTTON, smallObjectTexture, context, 0, 0, 2, 1);
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
