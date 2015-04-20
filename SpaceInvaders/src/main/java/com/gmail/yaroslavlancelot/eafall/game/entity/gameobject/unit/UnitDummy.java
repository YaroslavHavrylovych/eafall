package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
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
    /**
     * path to unit sprite image (small one) so it can be
     * retrieved separate from unit creation if needed
     */
    protected final String mPathToSprite;
    /**
     * path to unit image (big one) so it can be
     * retrieved separate from unit creation if needed
     */
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
    /** unit sprite texture region */
    protected ITextureRegion mSpriteTextureRegion;
    /** unit image texture region */
    protected ITextureRegion mImageTextureRegion;
    /** you can get unit name from the string resources by this id */
    private int mUnitStringId;

    public UnitDummy(UnitLoader unitLoader, String allianceName) {
        mUnitLoader = unitLoader;
        mPathToSprite = StringConstants
                .getPathToUnits(allianceName.toLowerCase()) + mUnitLoader.name + ".png";
        mPathToImage = StringConstants
                .getPathToUnits_Image(allianceName.toLowerCase()) + mUnitLoader.name + ".png";
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

    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        mSpriteTextureRegion = TextureRegionHolder
                .addElementFromAssets(mPathToSprite, textureAtlas, context, x, y);
    }

    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        mImageTextureRegion = TextureRegionHolder.
                addElementFromAssets(mPathToImage, textureAtlas, context, x, y);
    }

    public abstract void initDummy(VertexBufferObjectManager objectManager,
                                   SoundOperations soundOperations, String allianceName);

    public abstract Unit constructUnit();

    /** create and return stationary unit builder */
    protected UnitBuilder initBuilder(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        UnitBuilder unitBuilder =
                createUnitBuilder(getSpriteTextureRegion(), soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(getReloadTime())
                .setSoundPath(StringConstants.getPathToSounds(allianceName.toLowerCase()) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setTeamColorArea(getTeamColorArea())
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

    public ITextureRegion getSpriteTextureRegion() {
        return mSpriteTextureRegion;
    }

    public ITextureRegion getImageTextureRegion() {
        return mImageTextureRegion;
    }

    public int getUnitStringId() {
        return mUnitStringId;
    }

    public float getReloadTime() {
        return mUnitLoader.reload_time;
    }
}
