package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.alliances.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.UnitLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.entity.shape.Area;
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
        mPathToImage = GameStringsConstantsAndUtils.getPathToUnits(Imperials.ALLIANCE_NAME) + mUnitLoader.name + ".png";
        mHeight = SizeConstants.UNIT_SIZE;
        mWidth = SizeConstants.UNIT_SIZE;

        mUnitDamage = new Damage(mUnitLoader.damage, mUnitLoader.damage_value);
        mUnitArmor = new Armor(mUnitLoader.armor, mUnitLoader.armor_value);

        TeamColorArea area = mUnitLoader.team_color_area;
        mTeamColorArea = new Area(area.x, area.y, area.width, area.height);
        mUnitLoader.team_color_area = null;
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        GameObject.loadResource(mPathToImage, context, textureAtlas, x, y);
        mTextureRegion = TextureRegionHolderUtils.getInstance().getElement(mPathToImage);
    }

    public Unit constructUnit(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        UnitBuilder unitBuilder = new UnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(GameStringsConstantsAndUtils.getPathToSounds(allianceName) + mUnitLoader.sound)
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

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public String getName() {
        return mUnitLoader.name;
    }
}
