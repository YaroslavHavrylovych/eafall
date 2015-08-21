package com.gmail.yaroslavlancelot.eafall.test

import android.test.InstrumentationTestRunner
import android.test.InstrumentationTestSuite
import com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.GameObjectTest
import com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.dynamic.MovableUnitTest
import junit.framework.TestSuite

/** run all tests  */
public class DefaultInstrumentationTestRunner : InstrumentationTestRunner() {
    override fun getAllTests(): TestSuite {
        val suite = InstrumentationTestSuite(this)

        //game objects
        suite.addTestSuite(javaClass<GameObjectTest>())
        suite.addTestSuite(javaClass<MovableUnitTest>())
        //popup
        //        suite.addTestSuite(javaClass<BuildingDescriptionAreaUpdaterTest>())

        return suite
    }
}
