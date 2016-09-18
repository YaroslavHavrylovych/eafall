package com.yaroslavlancelot.eafall.test

import android.test.InstrumentationTestRunner
import android.test.InstrumentationTestSuite
import com.yaroslavlancelot.eafall.test.game.GameStateTest
import com.yaroslavlancelot.eafall.test.game.entity.gameobject.GameObjectTest
import com.yaroslavlancelot.eafall.test.game.entity.gameobject.equipment.DamageArmorTest
import com.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.dynamic.OffenceUnitTest
import com.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.filtering.SquareUnitMapTest
import com.yaroslavlancelot.eafall.test.game.popup.PopupSceneTest
import junit.framework.TestSuite

/** run all tests  */
class DefaultInstrumentationTestRunner : InstrumentationTestRunner() {
    override fun getAllTests(): TestSuite {
        val suite = InstrumentationTestSuite(this)

        //game objects
        suite.addTestSuite(SquareUnitMapTest::class.java)
        suite.addTestSuite(GameObjectTest::class.java)
        suite.addTestSuite(OffenceUnitTest::class.java)
        suite.addTestSuite(DamageArmorTest::class.java)
        //game state
        suite.addTestSuite(GameStateTest::class.java)
        //popup
        suite.addTestSuite(PopupSceneTest::class.java)

        return suite
    }
}
