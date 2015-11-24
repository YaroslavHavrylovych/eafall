package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.explosion;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;

import org.andengine.entity.sprite.AnimatedSprite;

/**
 * Unit explosion animation listener. Used to perform actions while animation.
 * Destroys unit at the beginning of the animation. Recycled animation at the end.
 *
 * @author Yaroslav Havrylovych
 */
public class UnitExplosionAnimationListener implements AnimatedSprite.IAnimationListener {
    private final UnitExplosionAnimation mAnimatedSprite;
    private GameObject mGameObject;

    UnitExplosionAnimationListener(UnitExplosionAnimation animatedSprite) {
        mAnimatedSprite = animatedSprite;
    }

    @Override
    public void onAnimationStarted(final AnimatedSprite pAnimatedSprite, final int pInitialLoopCount) {
        mGameObject.destroy();
    }

    @Override
    public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex) {
    }

    @Override
    public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount) {
    }

    @Override
    public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
        mAnimatedSprite.setPosition(-100, -100);
        mAnimatedSprite.setVisible(false);
        mAnimatedSprite.setIgnoreUpdate(true);
        UnitExplosionPool.getInstance().recycle(mAnimatedSprite);
    }

    public void init(GameObject gameObject) {
        mGameObject = gameObject;
    }
}
