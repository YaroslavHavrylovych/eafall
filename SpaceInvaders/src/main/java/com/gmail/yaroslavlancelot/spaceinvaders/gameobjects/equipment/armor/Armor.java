package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;

/**
 * Represent unit armor. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage.DamageType}.
 */
public class Armor {
    private static float[][] sArmorSafetyTable;
    private int mArmorValue;
    private ArmorType mArmorType;

    public Armor(String armorType, final int armorValue) {
        mArmorValue = armorValue;
        mArmorType = ArmorType.valueOf(armorType.toUpperCase());
    }

    public static void initSafetyTable() {
        sArmorSafetyTable = new float[Damage.DamageType.values().length][];
        //RAILGUN
        sArmorSafetyTable[0] = new float[]{1.5f, .85f, 1f, 1.2f, .45f, .45f};
        //LASER
        sArmorSafetyTable[1] = new float[]{1.2f, .9f, 1.2f, .7f, 1f, .7f};
        //Neutrino blaster
        sArmorSafetyTable[2] = new float[]{1f, 1.05f, 1.4f, .6f, .95f, .6f};
        //Higgson
        sArmorSafetyTable[3] = new float[]{1f, 1f, 1f, 1f, 1f, 1f};
        //Annihilator
        sArmorSafetyTable[4] = new float[]{1f, 1.1f, .75f, .95f, 1.2f, .75f};
        //Quaker
        sArmorSafetyTable[3] = new float[]{.3f, .5f, .45f, .6f, .5f, 1.75f};
    }

    public int getDamage(Damage damage) {
        int realDamage = getDamageAfterConsumption(damage);
        return realDamage * realDamage / (realDamage + mArmorValue) + 1;
    }

    protected int getDamageAfterConsumption(Damage damage) {
        return (int) (damage.getDamageValue() *
                sArmorSafetyTable[damage.getDamageType().ordinal()][mArmorType.ordinal()]);
    }

    public int getArmorValue() {
        return mArmorValue;
    }

    public ArmorType getArmorType() {
        return mArmorType;
    }

    public static enum ArmorType {
        UNARMORED, PHYSICAL, ELECTROMAGNETIC, CHERENKOV, HIGGS_SHIELD, MIXED;
    }
}
