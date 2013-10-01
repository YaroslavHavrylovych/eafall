package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

/** if some class extends this interface, this will marks objects as touchable */
public interface ISpriteTouchable {
    /**
     * set on touch for class
     *
     * @param spriteTouchListener new {@link ISpriteTouchListener}
     */
    public void setOnTouchListener(ISpriteTouchListener spriteTouchListener);
}
