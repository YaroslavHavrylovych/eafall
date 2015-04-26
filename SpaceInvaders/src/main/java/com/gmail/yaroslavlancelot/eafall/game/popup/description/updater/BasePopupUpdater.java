package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Description popup can present different objects. General actions for all of them you
 * can use with extending current class
 */
public abstract class BasePopupUpdater implements IPopupUpdater {
    protected final VertexBufferObjectManager mVertexBufferObjectManager;
    /** left side sprite (showPopup descript object image) in it's area */
    protected Sprite mObjectImage;
    /** used by children classes */
    protected Scene mScene;

    public BasePopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        mVertexBufferObjectManager = vertexBufferObjectManager;
        mScene = scene;
    }

    @Override
    public void updateImage(Shape drawArea, Object objectId, String allianceName, String teamName) {
        if (mObjectImage != null) {
            drawArea.detachChild(mObjectImage);
        }
        ITextureRegion textureRegion = getDescriptionImage(objectId, allianceName);
        if (textureRegion == null) {
            return;
        }
        mObjectImage = new Sprite(drawArea.getWidth() / 2, drawArea.getHeight() / 2,
                drawArea.getWidth(), drawArea.getHeight(),
                textureRegion, mVertexBufferObjectManager);
        drawArea.attachChild(mObjectImage);
    }

    @Override
    public void updateObjectNameText(Text text, Object objectId, String allianceName) {
        text.setText(getDescribedObjectName(objectId, allianceName));
    }

    /** return description object name {@link java.lang.String} */
    protected abstract String getDescribedObjectName(Object objectId, String allianceName);

    @Override
    public void clear() {
        if (mObjectImage != null) {
            mObjectImage.detachSelf();
            mObjectImage = null;
        }
    }

    /** return description image */
    protected abstract ITextureRegion getDescriptionImage(Object objectId, String allianceName);
}
