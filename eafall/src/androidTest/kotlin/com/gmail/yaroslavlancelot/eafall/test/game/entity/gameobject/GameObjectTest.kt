package com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject

import android.test.AndroidTestCase
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject
import org.andengine.util.math.MathConstants
import kotlin.test.assertTrue

/**
 * @author Yaroslav Havrylovych
 */
class GameObjectTest : AndroidTestCase() {
    public fun testDirection() {
        val getAngle: (Float, Float, Float, Float) -> Float =
                { x1, y1, x2, y2 -> GameObject.getDirection(x1, y1, x2, y2) }
        var angle = getAngle(0f, 0f, 5f, 10f);
        assertTrue(0 < angle && angle < MathConstants.PI / 4f, "0 < a < pi/4");
        angle = getAngle(10f, 10f, 15f, 20f);
        assertTrue(0 < angle && angle < MathConstants.PI / 4, "0 < a < pi/4");
        angle = getAngle(10f, 10f, 20f, 15f);
        assertTrue(MathConstants.PI / 4 < angle && angle < MathConstants.PI / 2, "pi/4 < a < pi/2");
        angle = getAngle(10f, 10f, 20f, 5f);
        assertTrue(MathConstants.PI / 2 < angle && angle < 3 * MathConstants.PI / 4, "pi/2 < a < 3*pi/4");
        angle = getAngle(10f, 10f, 15f, 0f);
        assertTrue(3 * MathConstants.PI / 4 < angle && angle < MathConstants.PI, "3*pi/4 < a < pi");
        angle = getAngle(10f, 10f, 5f, 0f);
        assertTrue(MathConstants.PI < angle && angle < 5 * MathConstants.PI / 4, "pi < a < 5*pi/4");
        angle = getAngle(10f, 10f, 0f, 5f);
        assertTrue(5 * MathConstants.PI / 4 < angle && angle < 3 * MathConstants.PI / 2, "5*pi/4 < a < 3*pi/2");
        angle = getAngle(10f, 10f, 0f, 15f);
        assertTrue(3 * MathConstants.PI / 2 < angle && angle < 7 * MathConstants.PI / 4, "3*pi/2 < a < 7*pi/4");
        angle = getAngle(10f, 10f, 5f, 20f);
        assertTrue(7 * MathConstants.PI / 4 < angle && angle < 2 * MathConstants.PI, "7*pi/4 < a < 2*pi");
        //change by 90
        assertTrue(getAngle(0f, 0f, 0f, 10f) == 0f, "a = 0");
        assertTrue(getAngle(0f, 0f, 10f, 0f) == MathConstants.PI / 2, "a = pi/2");
        assertTrue(getAngle(10f, 10f, 10f, 0f) == MathConstants.PI, "a = pi");
        assertTrue(getAngle(10f, 10f, 0f, 10f) == 3 * MathConstants.PI / 2, "a = 3*pi/2");
        assertTrue(getAngle(10f, 10f, 10f, 20f) == 0f, "a = 0");
    }
}