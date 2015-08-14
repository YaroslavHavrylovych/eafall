package com.gmail.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.entity.sprite.batch.vbo.ISpriteBatchVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Has alpha for children
 *
 * @author Yaroslav Havrylovych
 */
public class AlphaToChildrenSpriteGroup extends SpriteGroup {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTexture, pCapacity, pVertexBufferObjectManager);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager);
    }

    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        super(pTexture, pCapacity, pVertexBufferObjectManager, pDrawType);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, pDrawType);
    }

    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
        super(pTexture, pCapacity, pVertexBufferObjectManager, pShaderProgram);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
        super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, pShaderProgram);
    }

    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        super(pTexture, pCapacity, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
        super(pTexture, pCapacity, pSpriteBatchVertexBufferObject);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
        super(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject);
    }

    public AlphaToChildrenSpriteGroup(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
        super(pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
    }

    public AlphaToChildrenSpriteGroup(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
        super(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void setAlpha(final float pAlpha) {
        super.setAlpha(pAlpha);
        if(mChildren != null) {
            for (IEntity child : mChildren) {
                child.setAlpha(pAlpha);
            }
        }
    }

    @Override
    public void attachChild(final Sprite pSprite) {
        super.attachChild(pSprite);
        pSprite.setAlpha(getAlpha());
    }

    @Override
    public void attachChildren(final ArrayList<? extends Sprite> pSprites) {
        super.attachChildren(pSprites);
        for (Sprite sprite : pSprites) {
            sprite.setAlpha(getAlpha());
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
