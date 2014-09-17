package org.andengine.entity.shape;

/** add custom touch event handler */
interface ITouchListener {
    /**
     * Add touch listener to an object. If touch event appears it will trigger
     * {@link org.andengine.entity.shape.ITouchCallback#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)}
     * of the passed {@code touchCallback} parameter. You can remove this touch listener (default will be in use then).
     */
    public void setTouchCallback(ITouchCallback touchCallback);

    /**
     * remove {@link org.andengine.entity.shape.ITouchCallback} (which intersect touch event) from current object
     */
    public void removeTouchCallback();
}
