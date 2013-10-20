package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

public class Physical extends Armor {
    public static final ArmorType ARMOR_TYPE = ArmorType.PHYSICAL;

    public Physical(final int armorValue) {
        super(armorValue);
    }

    @Override
    protected int getDamageAfterConsumption() {
        return 0;
    }

    @Override
    public ArmorType getArmorType() {
        return null;
    }
}
