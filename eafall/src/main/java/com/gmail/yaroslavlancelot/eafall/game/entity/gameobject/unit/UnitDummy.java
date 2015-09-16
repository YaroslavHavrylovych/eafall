package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundOperations;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.ColorSwapBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
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
    /** unit damage */
    protected final Damage mUnitDamage;
    /** unit armor */
    protected final Armor mUnitArmor;
    /** unit image texture region */
    protected ITextureRegion mImageTextureRegion;
    /** unit shout sound */
    protected LimitedSoundWrapper mFireSound;
    /** you can get unit name from the string resources by this id */
    private int mUnitStringId;

    public UnitDummy(UnitLoader unitLoader, String allianceName) {
        mUnitLoader = unitLoader;
        mPathToSprite = StringConstants
                .getPathToUnits(allianceName.toLowerCase()) + mUnitLoader.name + ".png";
        mPathToImage = StringConstants
                .getPathToUnits_Image(allianceName.toLowerCase()) + mUnitLoader.name + ".png";

        mUnitDamage = new Damage(mUnitLoader.damage, mUnitLoader.damage_value);
        mUnitArmor = new Armor(mUnitLoader.armor, mUnitLoader.armor_value);

        Context context = EaFallApplication.getContext();
        mUnitStringId = context.getResources().getIdentifier(
                mUnitLoader.name, "string", context.getApplicationInfo().packageName);
    }

    public int getHealth() {
        return mUnitLoader.health;
    }

    public float getReloadTime() {
        return mUnitLoader.reload_time;
    }

    public Damage getDamage() {
        return mUnitDamage;
    }

    public Armor getArmor() {
        return mUnitArmor;
    }

    public int getId() {
        return mUnitLoader.id;
    }

    public ITextureRegion getImageTextureRegion() {
        return mImageTextureRegion;
    }

    public int getUnitStringId() {
        return mUnitStringId;
    }

    public ITextureRegion loadSpriteResources(String playerName, BitmapTextureAtlas textureAtlas,
                                              int x, int y) {
        IBitmapTextureAtlasSource source = AssetBitmapTextureAtlasSource.create(EaFallApplication
                .getContext().getAssets(), mPathToSprite);
        ColorSwapBitmapTextureAtlasSource textureSource = new ColorSwapBitmapTextureAtlasSource
                (source, EaFallApplication.getConfig().getPlayerSwapColor(),
                        PlayersHolder.getPlayer(playerName).getColor());
        return TextureRegionHolder.addElementFromSource(getTextureRegionKey(playerName),
                textureAtlas, textureSource, x, y);
    }

    public void loadImageResources(Context context, BitmapTextureAtlas
            textureAtlas, int x, int y) {
        mImageTextureRegion = TextureRegionHolder.
                addElementFromAssets(mPathToImage, textureAtlas, context, x, y);
    }

    public String getTextureRegionKey(String playerName) {
        return mPathToSprite + playerName;
    }

    public void initDummy(SoundOperations soundOperations, String allianceName) {
        //if null then the game sound disabled
        final String soubdPath = StringConstants.getSoundsPath(allianceName.toLowerCase()) + mUnitLoader.sound;
        mFireSound = soundOperations.loadSound(soubdPath);
    }

    /** create and return stationary unit builder */
    public UnitBuilder createBuilder(final ITextureRegion spriteTextureRegion,
                                     VertexBufferObjectManager objectManager) {
        UnitBuilder unitBuilder =
                createUnitBuilder(spriteTextureRegion, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(getReloadTime())
                .setFireSound(mFireSound)
                .setDamage(getDamage())
                .setArmor(getArmor());

        return unitBuilder;
    }

    protected abstract UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                                     VertexBufferObjectManager objectManager);
}
