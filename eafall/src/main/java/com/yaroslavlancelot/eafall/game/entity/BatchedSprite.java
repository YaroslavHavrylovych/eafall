package com.yaroslavlancelot.eafall.game.entity;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;

/**
 * Custom {@link Sprite} aimed to be used in SpriteBatches. Can't have real children
 * but emulate their behaviour (i.e. when you try to attach a child, the batched
 * sprite will send an event to attach this entity to the sprite group on position of the
 * real child should be). This semi existing children work because of the best performance
 * you can achieve only with removing children from the sprite.
 * <br/>
 * NOTE:
 * <br/>
 * 1. BatchedSprite can only have the children which are BatchedSprite as well.
 * <br/>
 * 2. BatchedSprite always assigned to particular SpriteGroup (batch).
 */
public class BatchedSprite extends Sprite {
    /**
     * all children will be emulated. So we wouldn't add them as real children but
     * contain they here and add them to the sprite group.
     * <br/>
     * Note : you have to init create list only on those who need this in other case it will
     * throw null pointer.
     */
    protected SmartList<BatchedSprite> mChildren;
    /** current sprite {@link org.andengine.entity.sprite.batch.SpriteGroup} name */
    private String mSpriteGroupName;


    /**
     * CHILDREN IGNORES UPDATES:
     * All children will ignore updates as SpriteGroups children should be
     * drawn as separated entities. I used to add it as a children but to draw in other
     * place where the paren is drawn (e.g. GameObject health bar added as a child
     * but drawn with GameObject in the place where he is drawn
     */
    public BatchedSprite(float pX, float pY, float pWidth, float pHeight,
                         ITextureRegion textureRegion,
                         VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, textureRegion, pVertexBufferObjectManager);
        setChildrenIgnoreUpdate(true);
        setChildrenVisible(false);
    }

    /**
     * Invokes {@link BatchedSprite#BatchedSprite(float, float, float, float, ITextureRegion, VertexBufferObjectManager)}
     * with width and height from the textureRegion
     */
    public BatchedSprite(float pX, float pY, ITextureRegion textureRegion,
                         VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, textureRegion.getWidth(), textureRegion.getHeight(),
                textureRegion, pVertexBufferObjectManager);
    }

    public String getSpriteGroupName() {
        return mSpriteGroupName;
    }

    public void setSpriteGroupName(String spriteGroupName) {
        mSpriteGroupName = spriteGroupName;
    }

    @Override
    public boolean detachSelf() {
        detachChildren();
        boolean result = super.detachSelf();
        setParent(null);
        return result;
    }

    @Override
    public void detachChildren() {
        if (mChildren != null) {
            for (int i = 0; i < mChildren.size(); i++) {
                IEntity child = mChildren.get(i);
                child.detachSelf();
                child.setParent(null);
            }
        }
    }

    @Override
    @Deprecated
    public void attachChild(IEntity pEntity) {
        BatchedSprite child = (BatchedSprite) pEntity;
        child.attachSelf();
        mChildren.add(child);
    }

    @Override
    public boolean detachChild(IEntity pEntity) {
        if (mChildren.remove(pEntity)) {
            pEntity.detachSelf();
            pEntity.setParent(null);
            return true;
        }
        return false;
    }

    public void addChild(BatchedSprite child) {
        mChildren.add(child);
    }

    public void attachChildren() {
        if (mChildren != null) {
            for (int i = 0; i < mChildren.size(); i++) {
                mChildren.get(i).attachSelf();
            }
        }
    }

    public void attachSelf() {
        SpriteGroupHolder.getGroup(getSpriteGroupName()).attachChild(this);
        attachChildren();
    }

    protected ITextureRegion loadResource(String pathToImage, BitmapTextureAtlas textureAtlas,
                                          Context context, int x, int y) {
        return TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }
}
