package com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.general.locale.Locale;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

/**
 * Represent unit damage. Used for calculate damage to unit depends on {@link com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor.ArmorType}
 * and {@link com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage.DamageType}.
 */
public class Damage {
    private int mDamageValue;
    private DamageType mDamageType;
    private volatile int mAdditionalDamage;

    public Damage(String damageType, int damageValue) {
        this(DamageType.valueOf(damageType.toUpperCase()), damageValue);
    }

    public Damage(DamageType damageType, int damageValue) {
        mDamageValue = damageValue;
        mDamageType = damageType;
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

    public String getString() {
        Locale locale = LocaleImpl.getInstance();
        int val;
        switch (mDamageType) {
            case RAILGUN:
                val = R.string.damage_railgun;
                break;
            case LASER:
                val = R.string.damage_laser;
                break;
            case NEUTRINO:
                val = R.string.damage_neutrino;
                break;
            case HIGGSON:
                val = R.string.damage_higgson;
                break;
            case ANNIHILATOR:
                val = R.string.damage_annihilator;
                break;
            case QUAKER:
            default:
                val = R.string.damage_quaker;
        }
        return locale.getStringById(val);
    }

    public void removeDamage() {
        mDamageValue = 0;
    }

    public enum DamageType {
        RAILGUN, LASER, NEUTRINO, HIGGSON, ANNIHILATOR, QUAKER
    }
}
