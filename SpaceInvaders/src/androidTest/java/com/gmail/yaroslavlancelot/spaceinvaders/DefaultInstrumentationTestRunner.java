package com.gmail.yaroslavlancelot.spaceinvaders;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit.BuildingDescriptionAreaUpdaterTest;

import junit.framework.TestSuite;

/** run all tests */
public class DefaultInstrumentationTestRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

        suite.addTestSuite(BuildingDescriptionAreaUpdaterTest.class);

        return suite;
    }
}
