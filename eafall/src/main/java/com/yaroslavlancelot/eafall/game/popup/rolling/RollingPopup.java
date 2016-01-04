package com.yaroslavlancelot.eafall.game.popup.rolling;

import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.popup.PopupScene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseStrongOut;

/** Base popups class. Touch on HUD except any hud element will cause popup closing */
public abstract class RollingPopup extends PopupScene implements IRollingPopup {
    private static final String TAG = RollingPopup.class.getCanonicalName();
    private final boolean mRollingFromBottom;
    protected Sprite mBackgroundSprite;
    private IEntityModifier mExpandModifier;
    private IEntityModifier mCollapseModifier;
    private boolean mRollingInProcess;
    private float mAnimationTime = 0.5f;

    public RollingPopup(Scene scene, Camera camera) {
        this(scene, camera, true);
    }

    public RollingPopup(Scene scene, Camera camera, boolean rollingFromBottom) {
        super(scene, camera);
        mRollingFromBottom = rollingFromBottom;
    }

    @Override
    public void showPopup() {
        if (isShowing() || mRollingInProcess) {
            return;
        }
        mRollingInProcess = true;
        mExpandModifier.reset();
        mBackgroundSprite.registerEntityModifier(mExpandModifier);
        attachPopupScene();
    }

    @Override
    public void hidePopup() {
        if (!isShowing() || mRollingInProcess) {
            return;
        }
        SoundFactory.getInstance().playSound(GeneralSoundKeys.TICK);
        mRollingInProcess = true;
        unregisterTouchArea(mBackgroundSprite);
        mCollapseModifier.reset();
        mBackgroundSprite.registerEntityModifier(mCollapseModifier);
    }

    @Override
    public void triggerPopup() {
        //TODO logger was here
        if (mIsPopupShowing) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    /**
     * The method init`s popup extend/collapse modifiers.
     * WARN: has to be invoked in the child constructor after
     * {@link RollingPopup#mBackgroundSprite} has been instantiated.
     */
    protected void init() {
        float half = mBackgroundSprite.getHeight() / 2;
        float fromY = mRollingFromBottom ? -half : SizeConstants.GAME_FIELD_HEIGHT + half;
        float toY = mRollingFromBottom ? half : SizeConstants.GAME_FIELD_HEIGHT - half;
        initExpandModifier(fromY, toY);
        initCollapseModifier(fromY, toY);
    }

    /** collapse the popup */
    private void initCollapseModifier(float fromY, float toY) {
        mCollapseModifier = new MoveYModifier(mAnimationTime, toY, fromY, EaseStrongOut.getInstance());
        mCollapseModifier.addModifierListener(new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> modifier, final IEntity item) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> modifier, final IEntity item) {
                mIsPopupShowing = false;
                mRollingInProcess = false;
                detachPopupScene();
                if (mStateChangingListener != null) {
                    mStateChangingListener.onHided();
                }
            }
        });
    }

    /** expand the popup */
    private void initExpandModifier(float fromY, float toY) {
        mExpandModifier = new MoveYModifier(mAnimationTime, fromY, toY, EaseStrongOut.getInstance());
        mExpandModifier.addModifierListener(new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> modifier, final IEntity item) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> modifier, final IEntity item) {
                registerTouchArea(mBackgroundSprite);
                mIsPopupShowing = true;
                mRollingInProcess = false;
                if (mStateChangingListener != null) {
                    mStateChangingListener.onShowed();
                }
            }
        });
    }
}
