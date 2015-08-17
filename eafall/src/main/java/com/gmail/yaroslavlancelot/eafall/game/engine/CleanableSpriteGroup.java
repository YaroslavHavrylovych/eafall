package com.gmail.yaroslavlancelot.eafall.game.engine;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Iterator;

/**
 * @author Yaroslav Havrylovych
 */
public class CleanableSpriteGroup extends SpriteGroup {
    // ===========================================================
    // Constants
    // ===========================================================

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
            LoggerHelper.printWarnMessage(this.getClass().getSimpleName(), "cleanable limit reached");
            for (Iterator<IEntity> iterator = mChildren.iterator(); iterator.hasNext(); ) {
                IEntity child = iterator.next();
                if (!child.isVisible()) {
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
