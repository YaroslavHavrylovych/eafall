package org.andengine.entity.scene;

import org.andengine.entity.shape.ITouchCallback;
import org.andengine.util.IMatcher;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Yaroslav Havrylovych (ITouchCallback)
 */
public interface ITouchArea extends ITouchCallback {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public boolean contains(final float pX, final float pY);

    public float[] convertSceneCoordinatesToLocalCoordinates(final float pX, final float pY);

    public float[] convertLocalCoordinatesToSceneCoordinates(final float pX, final float pY);

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
