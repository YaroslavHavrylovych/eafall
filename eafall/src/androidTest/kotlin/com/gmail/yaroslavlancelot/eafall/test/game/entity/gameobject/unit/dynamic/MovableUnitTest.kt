package com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.dynamic

import com.gmail.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnitBuilder
import com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit.UnitTest
import org.andengine.engine.handler.IUpdateHandler
import org.andengine.entity.modifier.RotationModifier
import org.andengine.opengl.texture.EmptyTexture
import org.andengine.opengl.texture.TextureManager
import org.andengine.opengl.texture.region.TextureRegion
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Yaroslav Havrylovych
 */
public class MovableUnitTest : UnitTest() {
    override fun createUnit(): TestableMovableUnit {
        val movableUnitBuilder = MovableUnitBuilder(
                TextureRegion(EmptyTexture(TextureManager(), 20, 20), 0f, 0f, 20f, 20f), null)
        movableUnitBuilder
                .setSpeed(20f)
                .setArmor(Armor(Armor.ArmorType.CHERENKOV, 5))
                .setDamage(Damage(Damage.DamageType.ANNIHILATOR, 25))
                .setAttackRadius(50)
                .setViewRadius(150)
                .setHealth(200)
                .setFireSound(LimitedSoundWrapper(null))
                .setReloadTime(1.5)
        return TestableMovableUnit(movableUnitBuilder)
    }

    override fun testRotation() {
        val unit = createUnit();
        assertTrue(unit.needRotation(45), "needRotation for 45 check")
        assertTrue(unit.needRotation(-10), "needRotation for -10 check")
        assertFalse(unit.needRotation(-4), "needRotation for -4 check")
        assertFalse(unit.needRotation(0), "needRotation for 0 check")
        unit.setRotation(0f)
        unit.setPosition(10f, 10f)
        assertTrue(unit.getAngle(20f, 0f) == 135, "getAngle for 45 check")
        assertTrue(unit.getAngle(20f, 20f) == 45, "getAngle for 45 check")
        assertTrue(unit.getAngle(10f, 20f) == 0, "getAngle for 0 check")
        assertTrue(unit.getAngle(0f, 20f) == -46, "getAngle for -46 check")
        assertFalse(unit.getAngle(0f, 20f) == 45, "getAngle for 45 (has to be wrong) check")
        assertTrue(unit.getAngle(0f, 0f) == -136, "getAngle for -136 check")
        unit.setRotation(90f)
        assertTrue(unit.getAngle(20f, 10f) == 0, "getAngle for 0 check")
        assertTrue(unit.getAngle(10f, 0f) == 90, "getAngle for 90 check")
        assertTrue(unit.getAngle(10f, 20f) == -90, "getAngle for -90 check")
    }

    public class TestableMovableUnit(unitBuilder: MovableUnitBuilder?) : MovableUnit(unitBuilder), UnitTest.TestableUnit {
        override public fun getAngle(x: Float, y: Float): Int {
            return super<MovableUnit>.getAngle(x, y)
        }

        override public fun needRotation(angle: Int): Boolean {
            return super<MovableUnit>.needRotation(angle)
        }

        override public fun rotateWithAngle(angle: Int) {
            super<MovableUnit>.rotateWithAngle(angle)
        }

        override fun getRotationModifier(): RotationModifier {
            return mUnitRotationModifier;
        }

        override fun getUpdateHandler(): IUpdateHandler {
            throw UnsupportedOperationException("not now")
        }

    }
}
