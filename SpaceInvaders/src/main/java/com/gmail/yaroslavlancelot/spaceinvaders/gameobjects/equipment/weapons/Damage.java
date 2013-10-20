package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

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
