package com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit

import android.test.AndroidTestCase
import org.andengine.engine.handler.IUpdateHandler
import org.andengine.entity.modifier.RotationModifier

/**
 * @author Yaroslav Havrylovych
 */
/** unit description object test  */
abstract class UnitTest : AndroidTestCase() {
    public fun testUpdatersRegistration() {
        val unit = createUnit()
    }

    abstract fun testRotation()

    abstract fun createUnit() : com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit

    public interface TestableUnit {
        fun getRotationModifier(): RotationModifier

        fun getUpdateHandler(): IUpdateHandler
    }
}
