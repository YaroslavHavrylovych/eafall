package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

/**
 * Represent unit damage. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage.DamageType}.
 */
public class Damage {
    private int mDamageValue;
    private DamageType mDamageType;
    private volatile int mAdditionalDamage;

    public Damage(String damageType, int damageValue) {
        mDamageValue = damageValue;
        mDamageType = DamageType.valueOf(damageType.toUpperCase());
    }

    public int getAdditionalDamage() {
        return mAdditionalDamage;
    }

    public void setAdditionalDamage(int additionalDamage) {
        mAdditionalDamage = additionalDamage;
    }

    public int getDamageValue() {
        return mDamageValue;
    }

    public DamageType getDamageType() {
        return mDamageType;
    }

    public void removeDamage() {
        mDamageValue = 0;
    }

    public static enum DamageType {
        RAILGUN, LASER, NEUTRINO, HIGGSON, ANNIHILATOR, QUAKER
    }
}
