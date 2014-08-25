package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.ShowBuildingDescriptionEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * Handle logic of redrawing and showing/hiding description popup.
 * Appears in the bottom of the screen when you want to create a building
 * or see unit (other object) characteristics.
 */
public class DescriptionPopup {
    public static final String TAG = DescriptionPopup.class.getCanonicalName();
    /** Single instance. Each object to display redraw content as it needs. */
    private static volatile DescriptionPopup sDescriptionPopup;
    /** scene to which popup attached */
    private Scene mScene;
    /** for not redraw already displayed object */
    private int mObjectNameId = Integer.MIN_VALUE;
    /** general elements of the popup (background sprite, close button, description image) */
    private BackgroundSprite mBackgroundSprite;

    /**
     * single instance that's why it's private constructor
     *
     * @param vertexBufferObjectManager object manager to create inner elements
     * @param scene                     popup will be attached to this scene
     */
    private DescriptionPopup(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        mScene = scene;
        mBackgroundSprite = new BackgroundSprite(vertexBufferObjectManager, scene);
        EventBus.getDefault().register(this);
    }

    /** singleton */
    public static synchronized DescriptionPopup getInstance() {
        return sDescriptionPopup;
    }

    /**
     * init this class so you can get its instance (singleton)
     *
     * @param objectManager object manager to create inner elements
     * @param scene         popup will be attached to this scene
     * @return
     */
    public static synchronized DescriptionPopup init(VertexBufferObjectManager objectManager, Scene scene) {
        sDescriptionPopup =
                new DescriptionPopup(objectManager, scene);

        return sDescriptionPopup;
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BackgroundSprite.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        BackgroundSprite.loadFonts(fontManager, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final ShowBuildingDescriptionEvent showBuildingDescriptionEvent) {
        if (mObjectNameId == showBuildingDescriptionEvent.mCreepBuildingDummy.getNameId()) {
            return;
        }
        CreepBuildingDummy dummy = showBuildingDescriptionEvent.mCreepBuildingDummy;
        mObjectNameId = dummy.getNameId();
        mBackgroundSprite.updateObjectImage(dummy.getTextureRegion(), showBuildingDescriptionEvent.mAmount);
        mBackgroundSprite.show();
    }

    public BackgroundSprite getPopupSprite() {
        return mBackgroundSprite;
    }
}
