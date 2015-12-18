package com.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Iterator;

/**
 * If {@link CleanableSpriteGroup} reach the capacity than it detaches all
 * entities which ignore updates.
 *
 * @author Yaroslav Havrylovych
 */
public class CleanableSpriteGroup extends SpriteGroup {
    // ===========================================================
    // Constants
    // ===========================================================
    /**
     * set this string as an {@link IEntity#setTag(int)} to mark object for recycling if
     * group capacity reached
     */
    public static final int RECYCLE = 4532;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public CleanableSpriteGroup(ITexture texture, int capacity,
                                VertexBufferObjectManager vertexManager) {
        super(texture, capacity, vertexManager);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void assertCapacity() {
        if (getChildCount() >= mCapacity) {
            //TODO logger was here
            for (Iterator<IEntity> iterator = mChildren.iterator(); iterator.hasNext(); ) {
                IEntity child = iterator.next();
                if (child.getTag() == RECYCLE) {
                    iterator.remove();
                    child.detachChildren();
                    child.setParent(null);
                }
            }
            if (this.getChildCount() >= this.mCapacity) {
                throw new IllegalStateException("This " + SpriteGroup.class.getSimpleName() + " has already reached its capacity (" + this.mCapacity + ") !");
            }
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
