package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;

/**
 * Represent unit armor. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage.DamageType}.
 */
public abstract class Armor {
    protected int mArmorValue;

    public Armor(final int armorValue) {
        mArmorValue = armorValue;
    }

    public int getDamage(Damage damage) {
        int realDamage = getDamageAfterConsumption(damage);
        return realDamage * realDamage / (realDamage + mArmorValue) + 1;
    }

    protected abstract int getDamageAfterConsumption(Damage damage);

    public abstract ArmorType getArmorType();

    public enum ArmorType {
        PHYSICAL, ELECTRICAL, MAGNETIC, HEAVY_WATER_SHIELDM, HIGGS_SHIELD
    }
}
