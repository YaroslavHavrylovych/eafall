package com.yaroslavlancelot.eafall.test.game

import android.test.AndroidTestCase
import junit.framework.Assert

/**
 * @author Yaroslav Havrylovych
 */
abstract class EaFallTestCase : AndroidTestCase() {
    protected fun assertTrue(condition: Boolean, message: String) {
        assertTrue(message, condition)
    }

    protected fun assertFalse(condition: Boolean, message: String) {
        assertFalse(message, condition)
    }

    protected fun assertNotNull(obj: Any, message: String) {
        Assert.assertNotNull(message, obj)
    }

    protected fun assertNull(obj: Any, message: String) {
        Assert.assertNull(message, obj)
    }

    protected fun assertEquals(obj1: Any, obj2: Any, message: String) {
        Assert.assertEquals(message, obj1, obj2)
    }
}
