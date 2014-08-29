package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** general actions which each updater can inherit */
public abstract class BaseDescriptionUpdater implements DescriptionUpdater {
    protected final VertexBufferObjectManager mVertexBufferObjectManager;
    /** left side sprite (show descript object image) in it's area */
    protected Sprite mObjectImage;

    public BaseDescriptionUpdater(VertexBufferObjectManager vertexBufferObjectManager) {
        mVertexBufferObjectManager = vertexBufferObjectManager;
    }

    @Override
    public void updateImage(RectangularShape drawArea, int objectId, String raceName, String teamName) {
        if (mObjectImage != null)
            drawArea.detachChild(mObjectImage);
        mObjectImage = new Sprite(0, 0, drawArea.getWidth(), drawArea.getHeight(),
                getDescriptionImage(objectId, raceName), mVertexBufferObjectManager);
        drawArea.attachChild(mObjectImage);
    }

    /** return description image */
    protected abstract ITextureRegion getDescriptionImage(int objectId, String raceName);
}
