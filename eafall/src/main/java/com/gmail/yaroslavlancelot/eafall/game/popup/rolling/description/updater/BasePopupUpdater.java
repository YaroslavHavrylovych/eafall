package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.spatial.bounds.util.FloatBoundsUtils;

/**
 * Description popup can present different objects. General actions for all of them you
 * can use with extending current class
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BasePopupUpdater implements IPopupUpdater {
    protected final VertexBufferObjectManager mVertexBufferObjectManager;
    /** left side sprite (showPopup descript object image) in it's area */
    protected Sprite mObjectImage;
    /** used by children classes */
    protected Scene mScene;
    /** used for different size operations */
    protected float[] tmpSizes = new float[2];

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
        mObjectImage = drawInArea(drawArea, textureRegion);
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

    @Override
    public void clear() {
        if (mObjectImage != null) {
            mObjectImage.detachSelf();
            mObjectImage = null;
        }
    }

    /**
     * Fill the given area with an image stored in texture region.
     * <br/>
     * New image width and height will be modified to fit the area
     *
     * @param area          area to draw an image
     * @param textureRegion texture region with an image
     * @return created entity which drawn in the area
     */
    protected Sprite drawInArea(IEntity area, ITextureRegion textureRegion) {
        tmpSizes[0] = textureRegion.getWidth();
        tmpSizes[1] = textureRegion.getHeight();
        FloatBoundsUtils.proportionallyBound(tmpSizes, area.getWidth());

        Sprite sprite = new Sprite(area.getWidth() / 2, area.getHeight() / 2,
                tmpSizes[0], tmpSizes[1], textureRegion, mVertexBufferObjectManager);
        area.attachChild(sprite);
        return sprite;
    }

    /** return description object name {@link java.lang.String} */
    protected abstract String getDescribedObjectName(Object objectId, String allianceName);

    /** return description image */
    protected abstract ITextureRegion getDescriptionImage(Object objectId, String allianceName);
}
