package org.andengine.entity.scene;

import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.IMatcher;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:01:18 - 27.03.2012
 */
public interface ITouchArea extends ITouchCallback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean contains(final float pX, final float pY);

	public float[] convertSceneToLocalCoordinates(final float pX, final float pY);
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITouchAreaMatcher extends IMatcher<ITouchArea> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}