package org.andengine.entity.shape;

import org.andengine.input.touch.TouchEvent;

/**
 * Touch event callback
 *
 * @author Yaroslav Havrylovych
 */
public interface ITouchCallback {
    /**
     * (documentation copied from {@link IShape}. Method moved to interface so we can use it separately)
     * This method only fires if this {@link org.andengine.entity.scene.ITouchArea} 
     * is registered to the {@link org.andengine.entity.scene.Scene}
     * via {@link org.andengine.entity.scene.Scene#registerTouchArea(org.andengine.entity.scene.ITouchArea)}.
     *
     * @param pSceneTouchEvent
     * @return <code>true</code> if the event was handled (that means {@link org.andengine.entity.scene.IOnAreaTouchListener}
     * of the {@link org.andengine.entity.scene.Scene} will not be fired!), otherwise <code>false</code>.
     */
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY);
}
