package org.andengine.entity.scene.background;



/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @author Yaroslav Havrylovych
 */
public class AutoParallaxBackground extends ParallaxBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mParallaxChangePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AutoParallaxBackground(final float pRed, final float pGreen, final float pBlue, final float pParallaxChangePerSecond) {
		super(pRed, pGreen, pBlue);

		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	/**
	 * change existing parallax value by value passed in params
	 *
	 * @param delta value which will be added to existing parallax value
	 */
	public void changeParallax(final float delta) {
		setParallaxValue(mParallaxValue + delta);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
