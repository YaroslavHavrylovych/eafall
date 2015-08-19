package com.gmail.yaroslavlancelot.eafall.test

import android.test.InstrumentationTestRunner
import android.test.InstrumentationTestSuite

import com.gmail.yaroslavlancelot.eafall.test.game.popup.description.updater.unit.BuildingDescriptionAreaUpdaterTest

import junit.framework.TestSuite

/** run all tests  */
public class DefaultInstrumentationTestRunner : InstrumentationTestRunner() {
    override fun getAllTests(): TestSuite {
        val suite = InstrumentationTestSuite(this)

        suite.addTestSuite(javaClass<BuildingDescriptionAreaUpdaterTest>())

        return suite
    }
}
