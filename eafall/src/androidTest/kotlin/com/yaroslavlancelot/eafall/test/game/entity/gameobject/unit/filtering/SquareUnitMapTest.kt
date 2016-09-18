package com.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.filtering

import com.yaroslavlancelot.eafall.game.constant.SizeConstants
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.SquareUnitMap
import com.yaroslavlancelot.eafall.test.game.EaFallTestCase
import com.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.dynamic.OffenceUnitTest
import java.util.*

/**
 * @author Yaroslav Havrylovych
 */
class SquareUnitMapTest constructor() : EaFallTestCase() {
    val LTR_UNIT_MAP = SquareUnitMap(true)
    val RTL_UNIT_MAP = SquareUnitMap(false)
    private val UNITS: ArrayList<OffenceUnitTest.TestableOffenceUnit>
    private val N: Int = LTR_UNIT_MAP.N
    private val M: Int = LTR_UNIT_MAP.M
    private val PARTITION_SIZE = LTR_UNIT_MAP.PARTITION_SIZE
    private val HALF_PARTITION_SIZE = LTR_UNIT_MAP.PARTITION_SIZE / 2
    val OFFENCE_UNIT_TEST = OffenceUnitTest()
    val ONE_RANGE = 120
    val TWO_RANGE = 220

    init {
        UNITS = ArrayList<OffenceUnitTest.TestableOffenceUnit>(30)
        //top
        constructUnit(0, 1, 1, 0, 0)//0
        constructUnit(1, 1, 1, 0, -20)//1
        constructUnit(2, 1, 2, 0, 0)//2
        constructUnit(3, 2, 0, 0, 0)//3
        constructUnit(4, 3, 1, 0, 0)//4
        constructUnit(5, 3, 3, 0, 0)//5
        constructUnit(6, 3, 3, 40, 0)//6
        //bottom
        constructUnit(7, N - 1, M - 1, 0, 0)//7
        constructUnit(8, N - 1, M - 1, 0, -20)//8
        constructUnit(9, N - 3, M - 1, 0, 0)//9
        constructUnit(10, N - 3, M - 1, 0, -20)//10
        constructUnit(11, N - 5, M - 2, -10, 0)//11
        constructUnit(12, N - 5, M - 2, 10, 0)//12
        constructUnit(13, N - 7, M - 2, 0, 0)//13
        constructUnit(14, N - 7, M - 2, -40, 0)//14
        constructUnit(15, N - 9, M - 4, 0, -40)//15
        //update maps
        val units = ArrayList<Unit>(UNITS.size)
        for (unit in UNITS) {
            units.add(unit)
        }
        LTR_UNIT_MAP.updatePositions(units)
        RTL_UNIT_MAP.updatePositions(units)
    }

    fun testRange() {
        var list = LTR_UNIT_MAP.getInRange(getX(4).toFloat(), getY(3).toFloat(), 120f)
        assertTrue(1 == list.size, "one unit has to be in range")
        list = RTL_UNIT_MAP.getInRange(getX(4).toFloat(), getY(3).toFloat(), 122f)
        assertTrue(2 == list.size, "two units has to be in range")
        list = RTL_UNIT_MAP.getInRange(getX(4).toFloat(), getY(3).toFloat(), 60f)
        assertTrue(0 == list.size, "unit found, but in shouldn't in the given range")
        list = RTL_UNIT_MAP.getInRange(getX(4).toFloat(), getY(3).toFloat(), 80f)
        assertTrue(0 == list.size, "unit found, but in shouldn't in the given range")
        list = LTR_UNIT_MAP.getInRange(getX(N - 3).toFloat(), getY(M - 1).toFloat(), 60f)
        assertTrue(2 == list.size, "unit found, but in shouldn't in the given range")
    }

    fun testLtrMap() {
        //top
        check(LTR_UNIT_MAP, 2, 0, 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 4, 2, 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 3, 3, 0, ONE_RANGE)
        check(LTR_UNIT_MAP, 4, 4, 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 6, 5, 3, TWO_RANGE)
        check(LTR_UNIT_MAP, -1, 5, 3, ONE_RANGE)
        //bottom
        check(LTR_UNIT_MAP, 7, N - 2, M - 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 9, N - 4, M - 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 11, N - 6, M - 2, ONE_RANGE)
        check(LTR_UNIT_MAP, -1, N - 9, M - 2, ONE_RANGE)
        check(LTR_UNIT_MAP, 14, N - 9, M - 2, TWO_RANGE)
    }

    private fun check(unitMap: IUnitMap, i: Int, n: Int, m: Int, range: Int) {
        val actualUnit = unitMap.getClosestUnit(getX(n).toFloat(), getY(m).toFloat(), range.toFloat())
        if (i == -1) {
            assertTrue(actualUnit == null, "expected null")
            return
        }
        val expectedUnit = UNITS[i]
        assertTrue(expectedUnit == actualUnit, "" + expectedUnit.tag + " expected but found " + actualUnit.tag)
    }

    fun testRtlMap() {
        //top
        check(RTL_UNIT_MAP, 0, 0, 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 2, 2, 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 3, 3, 0, ONE_RANGE)
        check(RTL_UNIT_MAP, 4, 4, 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 6, 5, 3, TWO_RANGE)
        check(RTL_UNIT_MAP, -1, 5, 3, ONE_RANGE)
        //bottom
        check(RTL_UNIT_MAP, 9, N - 2, M - 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 12, N - 4, M - 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 13, N - 6, M - 2, ONE_RANGE)
        check(RTL_UNIT_MAP, -1, N - 9, M - 2, ONE_RANGE)
        check(RTL_UNIT_MAP, 14, N - 9, M - 2, TWO_RANGE)
    }

    private fun getX(n: Int): Int {
        return n * PARTITION_SIZE + HALF_PARTITION_SIZE
    }

    private fun getY(m: Int): Int {
        return SizeConstants.GAME_FIELD_HEIGHT - m * PARTITION_SIZE - HALF_PARTITION_SIZE
    }

    private fun constructUnit(id: Int, n: Int, m: Int, dX: Int, dY: Int) {
        val unit = OFFENCE_UNIT_TEST.createUnit()
        unit.setPosition((n * PARTITION_SIZE + HALF_PARTITION_SIZE + dX).toFloat(),
                (SizeConstants.GAME_FIELD_HEIGHT - m * PARTITION_SIZE - HALF_PARTITION_SIZE + dY).toFloat())
        unit.tag = id
        UNITS.add(unit)
    }
}
