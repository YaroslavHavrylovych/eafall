package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import org.andengine.input.touch.TouchEvent;

/** object touch listener */
public interface ISpriteTouchListener {
    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
}
