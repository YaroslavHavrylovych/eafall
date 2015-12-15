package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.Selectable;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

/**
 * Used to give an access to selectors. Possible extending to different selectors that's the
 * reason of the factory using.
 *
 * @author Yaroslav Havrylovych
 */
public class SelectorFactory extends SelfCleanable implements Selector {
    private final static float MAX_ALPHA = .85f;
    // ===========================================================
    // Constants
    // ===========================================================
    private static SelectorFactory INSTANCE;
    // ===========================================================
    // Fields
    // ===========================================================
    private Sprite mSpriteSelector;
    private IEntityModifier mLightSelectorModifier = new AlphaModifier(2f, MAX_ALPHA, 0f);
    private IEntityModifier mShadowSelectorModifier = new AlphaModifier(2f, 0f, MAX_ALPHA);
    private boolean mBlocked;

    // ===========================================================
    // Constructors
    // ===========================================================
    private SelectorFactory() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public static Selector getSelector() {
        return INSTANCE;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean isBlocked() {
        return mBlocked;
    }

    @Override
    public void select(final Selectable object) {
        deselect();
        mLightSelectorModifier.reset();
        mSpriteSelector.registerEntityModifier(mLightSelectorModifier);
        mSpriteSelector.setPosition(object.getX(), object.getY());
        mSpriteSelector.setWidth(object.getWidth());
        mSpriteSelector.setHeight(object.getHeight());
        mSpriteSelector.setIgnoreUpdate(false);
    }

    @Override
    public void deselect() {
        mSpriteSelector.unregisterEntityModifier(mLightSelectorModifier);
        mSpriteSelector.unregisterEntityModifier(mShadowSelectorModifier);
        mSpriteSelector.setAlpha(0f);
        mSpriteSelector.setIgnoreUpdate(true);
    }

    @Override
    public void block() {
        mBlocked = true;
    }

    @Override
    public void unblock() {
        mBlocked = false;
    }

    @Override
    public void highlight(final Selectable object) {
        deselect();
        mSpriteSelector.setPosition(object.getX(), object.getY());
        mSpriteSelector.setWidth(object.getWidth());
        mSpriteSelector.setHeight(object.getHeight());
        mSpriteSelector.setIgnoreUpdate(false);
        mSpriteSelector.setAlpha(MAX_ALPHA);
    }

    @Override
    public void clear() {
        INSTANCE = null;
        mSpriteSelector = null;
        mLightSelectorModifier = null;
        mShadowSelectorModifier = null;
        mBlocked = false;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private Sprite init(VertexBufferObjectManager mVbo) {
        mSpriteSelector = new Sprite(0, 0,
                TextureRegionHolder.getRegion(StringConstants.FILE_SELECTOR), mVbo);
        mLightSelectorModifier.addModifierListener(new IModifier.IModifierListener<IEntity>() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
                mShadowSelectorModifier.reset();
                mSpriteSelector.registerEntityModifier(mShadowSelectorModifier);
            }
        });
        mShadowSelectorModifier.addModifierListener(new IModifier.IModifierListener<IEntity>() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
                mLightSelectorModifier.reset();
                mSpriteSelector.registerEntityModifier(mLightSelectorModifier);
            }
        });
        mSpriteSelector.setAlpha(0f);
        mSpriteSelector.setIgnoreUpdate(true);
        return mSpriteSelector;
    }

    public static void loadResources(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        //background
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.PLANET_DIAMETER, SizeConstants.PLANET_DIAMETER,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.FILE_SELECTOR, textureAtlas, context, 0, 0);
        textureAtlas.load();
        //instantiate single instance
        INSTANCE = new SelectorFactory();
        INSTANCE.mBlocked = false;
        INSTANCE.mLightSelectorModifier = new AlphaModifier(1.5f, MAX_ALPHA, 0f);
        INSTANCE.mShadowSelectorModifier = new AlphaModifier(1.5f, 0f, MAX_ALPHA);
    }

    public static void init(VertexBufferObjectManager mVbo, Scene scene) {
        scene.attachChild(INSTANCE.init(mVbo));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
