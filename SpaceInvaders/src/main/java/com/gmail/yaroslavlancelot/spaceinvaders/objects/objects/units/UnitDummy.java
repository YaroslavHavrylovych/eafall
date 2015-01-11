package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.units.UnitLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Contains data needed to create units (creation logic should be defined by children).
 * Additionally contains data which can be used without unit creation (path to image, damage etc)
 */
public abstract class UnitDummy {
    /** data loaded from xml which store units data (in string format) */
    protected final UnitLoader mUnitLoader;
    /** path to unit image so it can be retrieved separate from unit creation if needed */
    protected final String mPathToImage;
    /** unit image height */
    protected final int mHeight;
    /** unit image width */
    protected final int mWidth;
    /** area which contains team colors */
    protected final Area mTeamColorArea;
    /** unit damage */
    protected final Damage mUnitDamage;
    /** unit armor */
    protected final Armor mUnitArmor;
    /** unit texture region (do not create it each time when u want to create unit) */
    protected ITextureRegion mTextureRegion;
    /** you can get unit name from the string resources by this id */
    private int mUnitStringId;

    public UnitDummy(UnitLoader unitLoader, String allianceName) {
        mUnitLoader = unitLoader;
        mPathToImage = StringsAndPathUtils.getPathToUnits(allianceName) + mUnitLoader.name + ".png";
        mHeight = SizeConstants.UNIT_SIZE;
        mWidth = SizeConstants.UNIT_SIZE;

        mUnitDamage = new Damage(mUnitLoader.damage, mUnitLoader.damage_value);
        mUnitArmor = new Armor(mUnitLoader.armor, mUnitLoader.armor_value);

        TeamColorArea area = mUnitLoader.team_color_area;
        mTeamColorArea = new Area(area.x, area.y, area.width, area.height);
        mUnitLoader.team_color_area = null;

        Context context = SpaceInvadersApplication.getContext();
        mUnitStringId = context.getResources().getIdentifier(
                mUnitLoader.name, "string", context.getApplicationInfo().packageName);
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        GameObject.loadResource(mPathToImage, context, textureAtlas, x, y);
        mTextureRegion = TextureRegionHolderUtils.getInstance().getElement(mPathToImage);
    }

    public abstract Unit constructUnit(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName);

    public int getHealth() {
        return mUnitLoader.health;
    }

    public Damage getDamage() {
        return mUnitDamage;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Armor getArmor() {
        return mUnitArmor;
    }

    public int getId() {
        return mUnitLoader.id;
    }

    public Area getTeamColorArea() {
        return mTeamColorArea;
    }

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public int getUnitStringId() {
        return mUnitStringId;
    }
}
