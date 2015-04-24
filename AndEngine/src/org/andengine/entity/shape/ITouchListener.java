package org.andengine.entity.shape;

/**
 * add touch event custom handler
 *
 * @author Yaroslav Havrylovych (minor changes)
 */
interface ITouchListener {
    /**
     * Adds touch listener to an object. If touch event appears it will trigger
     * {@link ITouchCallback#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)}
     * of the passed {@code touchCallback} parameter. You can remove this touch listener (default will be in use then).
     */
    void setTouchCallback(ITouchCallback touchCallback);

    /**
     * remove current object {@link ITouchCallback}
     */
    void removeTouchCallback();
}
