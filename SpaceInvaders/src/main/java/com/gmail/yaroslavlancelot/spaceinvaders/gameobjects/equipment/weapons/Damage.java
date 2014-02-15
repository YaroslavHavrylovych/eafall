package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

/**
 * Represent unit damage. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage.DamageType}.
 */
public abstract class Damage {
    protected int mDamageValue;

    public Damage(int damageValue) {
        mDamageValue = damageValue;
    }

    public int getDamageValue() {
        return mDamageValue;
    }

    public abstract DamageType getDamageType();

    public enum DamageType {
        ANNIHILATOR, ELECTRICAL, MAGNETIC, NEUTRINO, HIGGS
    }
}
