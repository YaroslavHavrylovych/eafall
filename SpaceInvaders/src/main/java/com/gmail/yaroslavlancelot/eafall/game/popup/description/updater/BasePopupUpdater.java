package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;

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
    public void updateImage(Shape drawArea, Object objectId, String allianceName, String playerName) {
        if (mObjectImage != null) {
            drawArea.detachChild(mObjectImage);
        }
        ITextureRegion textureRegion = getDescriptionImage(objectId, allianceName);
        if (textureRegion == null) {
            return;
        }
        int doubled_padding = 2 * SizeConstants.DESCRIPTION_POPUP_IMAGE_PADDING;
        int width = (int) drawArea.getWidth() - doubled_padding;
        int height = (int) drawArea.getHeight() - doubled_padding;
        mObjectImage = new Sprite(drawArea.getWidth() / 2, drawArea.getHeight() / 2,
                width, height,
                textureRegion, mVertexBufferObjectManager);
        drawArea.attachChild(mObjectImage);
    }

    @Override
    public void updateObjectNameText(Text text, Object objectId, String allianceName) {
        String value = getDescribedObjectName(objectId, allianceName);
        //for multiple words, last always on the next row
        int ind = value.lastIndexOf(" ");
        if (ind != -1) {
            value = new StringBuilder(value).replace(ind, ind + 1, "\n").toString();
        }
        text.setText(value);
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
