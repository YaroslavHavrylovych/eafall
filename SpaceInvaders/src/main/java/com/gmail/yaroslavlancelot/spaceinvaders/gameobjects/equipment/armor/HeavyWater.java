package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;

public class HeavyWater extends Armor {
    public static final ArmorType ARMOR_TYPE = ArmorType.HEAVY_WATER_SHIELDM;

    public HeavyWater(final int armorValue) {
        super(armorValue);
    }

    @Override
    protected int getDamageAfterConsumption(Damage damage) {
        Damage.DamageType damageType = damage.getDamageType();
        if(damageType.equals(Damage.DamageType.ANNIHILATOR))
            return (int) (damage.getDamageValue() * 1);
        if(damageType.equals(Damage.DamageType.ELECTRICAL))
            return (int) (damage.getDamageValue() * .9);
        if(damageType.equals(Damage.DamageType.MAGNETIC))
            return (int) (damage.getDamageValue() * .9);
        if(damageType.equals(Damage.DamageType.NEUTRINO))
            return (int) (damage.getDamageValue() * .7);
        if(damageType.equals(Damage.DamageType.HIGGS))
            return (int) (damage.getDamageValue() * .8);
        throw new IllegalArgumentException("unsupported damage type");
    }

    @Override
    public ArmorType getArmorType() {
        return ARMOR_TYPE;
    }
}
