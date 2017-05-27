package com.yaroslavlancelot.eafall.test.game.entity.gameobject.equipment

import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage
import com.yaroslavlancelot.eafall.test.game.EaFallTestCase

/**
 * @author Yaroslav Havrylovych
 */
class DamageArmorTest : EaFallTestCase() {
    fun testDamageConsumption() {
        var damage = Damage(Damage.DamageType.ANNIHILATOR, 20)
        var armor = Armor(Armor.ArmorType.ELECTROMAGNETIC, 20)
        assertEquals(12, armor.getDamage(damage), "miscalculations in damage to armor")
        damage = Damage(Damage.DamageType.HIGGSON, 20)
        assertEquals(16, armor.getDamage(damage), "miscalculations in damage to armor")
        damage = Damage(Damage.DamageType.NEUTRINO, 20)
        assertEquals(22, armor.getDamage(damage), "miscalculations in damage to armor")
        //other type
        damage = Damage(Damage.DamageType.RAILGUN, 20)
        armor = Armor(Armor.ArmorType.UNARMORED, 25)
        assertEquals(22, armor.getDamage(damage), "miscalculations in damage to armor")
        damage = Damage(Damage.DamageType.LASER, 20)
        assertEquals(14, armor.getDamage(damage), "miscalculations in damage to armor")
        damage = Damage(Damage.DamageType.QUAKER, 20)
        assertEquals(7, armor.getDamage(damage), "miscalculations in damage to armor")
    }

    fun testAdditionalArmorAndDamage() {
        var damage = Damage(Damage.DamageType.ANNIHILATOR, 20)
        var armor = Armor(Armor.ArmorType.CHERENKOV, 20)
        assertEquals(15, armor.getDamage(damage), "miscalculations in damage to armor")
        armor.setAdditionalArmor(10)
        assertEquals(13, armor.getDamage(damage), "miscalculations in damage to armor")
        damage.additionalDamage = 20
        assertEquals(26, armor.getDamage(damage), "miscalculations in damage to armor")
        //other type
        damage = Damage(Damage.DamageType.RAILGUN, 20)
        armor = Armor(Armor.ArmorType.HIGGS_SHIELD, 10)
        assertEquals(8, armor.getDamage(damage), "miscalculations in damage to armor")
        armor.setAdditionalArmor(10)
        assertEquals(7, armor.getDamage(damage), "miscalculations in damage to armor")
        damage.additionalDamage = 20
        assertEquals(14, armor.getDamage(damage), "miscalculations in damage to armor")
    }
}
