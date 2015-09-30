package com.gmail.yaroslavlancelot.eafall.test

import android.test.InstrumentationTestRunner
import android.test.InstrumentationTestSuite
import com.gmail.yaroslavlancelot.eafall.test.game.GameStateTest
import com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.GameObjectTest
import com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.dynamic.OffenceUnitTest
import com.gmail.yaroslavlancelot.eafall.test.game.popup.PopupSceneTest
import junit.framework.TestSuite

/** run all tests  */
public class DefaultInstrumentationTestRunner : InstrumentationTestRunner() {
    override fun getAllTests(): TestSuite {
        val suite = InstrumentationTestSuite(this)

        //game objects
        suite.addTestSuite(GameObjectTest::class.java)
        suite.addTestSuite(OffenceUnitTest::class.java)
        //game state
        suite.addTestSuite(GameStateTest::class.java)
        //popup
        suite.addTestSuite(PopupSceneTest::class.java)

        return suite
    }
}
