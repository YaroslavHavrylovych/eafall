package com.gmail.yaroslavlancelot.spaceinvaders.utils.touch;

import org.andengine.input.touch.TouchEvent;

public interface ISpriteTouchListener {
    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
}
