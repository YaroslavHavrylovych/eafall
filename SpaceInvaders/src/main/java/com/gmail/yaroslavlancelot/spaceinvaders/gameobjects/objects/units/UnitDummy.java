package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Electrical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.HeavyWater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Magnetic;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Physical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.UnitLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Contains data needed to create units (wrap creation logic).
 * Additionally contains data which can be used without unit creation (path to image, damage etc)
 */
public class UnitDummy {
    /** data loaded from xml which store units data (in string format) */
    private final UnitLoader mUnitLoader;
    /** path to unit image so it can be retrieved separate from unit creation if needed */
    private final String mPathToImage;
    /** unit image height */
    private final int mHeight;
    /** unit image width */
    private final int mWidth;
    /** area which contains team colors */
    private final Area mTeamColorArea;
    /** unit damage */
    private final Damage mUnitDamage;
    /** unit armor */
    private final Armor mUnitArmor;
    /** unit texture region (do not create it each time when u want to create unit) */
    private ITextureRegion mTextureRegion;

    public UnitDummy(UnitLoader unitLoader) {
        mUnitLoader = unitLoader;
        mPathToImage = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + mUnitLoader.name + ".png";
        mHeight = SizeConstants.UNIT_SIZE;
        mWidth = SizeConstants.UNIT_SIZE;

        mUnitDamage = new Damage(mUnitLoader.damage, mUnitLoader.damage_value);
        mUnitArmor = getUnitArmor(mUnitLoader.armor_value, mUnitLoader.armor);

        TeamColorArea area = mUnitLoader.team_color_area;
        mTeamColorArea = new Area(area.x, area.y, area.width, area.height);
        mUnitLoader.team_color_area = null;
    }

    private static Armor getUnitArmor(int value, String name) {
        switch (Armor.ArmorType.valueOf(name.toUpperCase())) {
            case PHYSICAL: {
                return new Physical(value);
            }
            case ELECTRICAL: {
                return new Electrical(value);
            }
            case HEAVY_WATER_SHIELD: {
                return new HeavyWater(value);
            }
            case HIGGS_SHIELD: {
                return new Higgs(value);
            }
            case MAGNETIC: {
                return new Magnetic(value);
            }
        }
        throw new IllegalArgumentException("unknown armor type");
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        GameObject.loadResource(mPathToImage, context, textureAtlas, x, y);
        mTextureRegion = TextureRegionHolderUtils.getInstance().getElement(mPathToImage);
    }

    public Unit constructUnit(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        UnitBuilder unitBuilder = new UnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + mUnitLoader.sound)
                .setDamage(mUnitDamage)
                .setWidth(getWidth())
                .setHeight(getHeight())
                .setArmor(mUnitArmor);

        return new Unit(unitBuilder);
    }

    public int getHealth() {
        return mUnitLoader.health;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Area getTeamColorArea() {
        return mTeamColorArea;
    }

    public Damage getDamage() {
        return mUnitDamage;
    }

    public Armor getUnitArmor() {
        return mUnitArmor;
    }
}
