package org.andengine.entity.shape;

/** add custom touch event handler */
interface ITouchListener {
    /**
     * Adds touch listener to an object. If touch event appears it will trigger
     * {@link ITouchCallback#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)}
     * of the passed {@code touchCallback} parameter. You can remove this touch listener (default will be in use then).
     */
    public void setTouchCallback(ITouchCallback touchCallback);

    /**
     * remove {@link ITouchCallback} (which intersect touch event) from current object
     */
    public void removeTouchCallback();
}
