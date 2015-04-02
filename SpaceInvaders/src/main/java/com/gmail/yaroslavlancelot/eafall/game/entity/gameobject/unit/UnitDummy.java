package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

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
        mPathToImage = StringConstants.getPathToUnits(allianceName.toLowerCase())
                + mUnitLoader.name + ".png";
        mHeight = SizeConstants.UNIT_SIZE;
        mWidth = SizeConstants.UNIT_SIZE;

        mUnitDamage = new Damage(mUnitLoader.damage, mUnitLoader.damage_value);
        mUnitArmor = new Armor(mUnitLoader.armor, mUnitLoader.armor_value);

        TeamColorArea area = mUnitLoader.team_color_area;
        mTeamColorArea = Area.getArea(area.x, area.y, area.width, area.height);
        mUnitLoader.team_color_area = null;

        Context context = EaFallApplication.getContext();
        mUnitStringId = context.getResources().getIdentifier(
                mUnitLoader.name, "string", context.getApplicationInfo().packageName);
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        BodiedSprite.loadResource(mPathToImage, context, textureAtlas, x, y);
        mTextureRegion = TextureRegionHolder.getInstance().getElement(mPathToImage);
    }

    public abstract void initDummy(VertexBufferObjectManager objectManager,
                                   SoundOperations soundOperations, String allianceName);

    public abstract Unit constructUnit();

    /** create and return stationary unit builder */
    protected UnitBuilder initBuilder(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        UnitBuilder unitBuilder = createUnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(StringConstants.getPathToSounds(allianceName.toLowerCase()) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setTeamColorArea(mTeamColorArea)
                .setHeight(getHeight())
                .setArmor(getArmor());

        return unitBuilder;
    }

    protected abstract UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                                     SoundOperations soundOperations,
                                                     VertexBufferObjectManager objectManager);

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

    public float getReloadTime() {
        return mUnitLoader.reload_time;
    }
}
