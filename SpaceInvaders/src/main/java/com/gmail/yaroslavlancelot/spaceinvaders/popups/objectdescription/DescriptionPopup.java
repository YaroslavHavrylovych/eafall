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
 * Contains elements which together create elements description (e.g. it's image, characteristics etc)
 * You can see it in the bottom of the screen after you tap on unit or building to build.
 * Contains element image and characteristics.
 */
public class DescriptionPopup {
    public static final String TAG = DescriptionPopup.class.getCanonicalName();
    private static volatile DescriptionPopup sDescriptionPopup;
    private Scene mScene;
    /** for not redraw already displayed object */
    private int mObjectNameId = Integer.MIN_VALUE;

    private BackgroundSprite mBackgroundSprite;

    private DescriptionPopup(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        mScene = scene;
        mBackgroundSprite = new BackgroundSprite(vertexBufferObjectManager, scene);
        EventBus.getDefault().register(this);
    }

    public static synchronized DescriptionPopup getInstance() {
        return sDescriptionPopup;
    }

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
