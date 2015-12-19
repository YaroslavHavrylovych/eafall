package com.yaroslavlancelot.eafall.game.resources.loaders.game;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.engine.ArgbColorComponentsSwapBitmapTextureAtlas;
import com.yaroslavlancelot.eafall.game.engine.CleanableSpriteGroup;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector.SelectorFactory;
import com.yaroslavlancelot.eafall.game.entity.health.PlayerHealthBar;
import com.yaroslavlancelot.eafall.game.entity.health.UnitHealthBar;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.yaroslavlancelot.eafall.game.resources.BaseResourceLoader;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Loads game resources excluding buildings, the star and planets.
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BaseGameObjectsLoader extends BaseResourceLoader {
    private static final ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart
            ORIGINAL_COLOR = ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart.B;
    private int mMovableUnitsLimit = 200;
    private int mMovableUnitsBuffer = 30;

    public BaseGameObjectsLoader() {
        addImage(StringConstants.FILE_BACKGROUND,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
    }

    public void setMovableUnitsLimit(int movableUnitsLimit) {
        mMovableUnitsLimit = movableUnitsLimit;
    }

    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        //selector
        SelectorFactory.loadResources(textureManager);
        //background
        loadBigImages(textureManager);
        //alliance
        loadAllianceSpecificResources(textureManager);
        //players
        loadPlayerSpecificResources(textureManager, vertexBufferObjectManager);
        //bullets and health bars
        loadBulletAndUnitHealthBar(textureManager, vertexBufferObjectManager);
        //explosions
        loadExplosions(textureManager, vertexBufferObjectManager);
        //other
        Context context = EaFallApplication.getContext();
        TextButton.loadResources(context, textureManager);
        RollingPopupManager.loadResource(context, textureManager);
        MenuPopupButton.loadResources(context, textureManager);
    }

    @Override
    public void unloadFonts(final FontManager fontManager) {
        throw new UnsupportedOperationException("fonts unloading not supported in the game");
    }

    @Override
    public void unloadImages(final TextureManager textureManager) {
        throw new UnsupportedOperationException("resources unloading not supported in the game");
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        super.loadFonts(textureManager, fontManager);
        RollingPopupManager.loadFonts(fontManager, textureManager);
        ClientGameHud.loadFonts(fontManager, textureManager);
    }

    protected abstract void loadAllianceSpecificResources(TextureManager textureManager);

    protected abstract void loadPlayerSpecificResources(TextureManager textureManager,
                                                        VertexBufferObjectManager vboManager);

    protected void loadPlayerSpecificBuildings(IAlliance alliance, VertexBufferObjectManager vboManager, IPlayer player) {
        TextureAtlas textureAtlas = alliance.getBuildingTextureAtlas();
        SpriteGroup spriteGroup = new SpriteGroup(textureAtlas,
                alliance.getBuildingsAmount(),
                vboManager);
        SpriteGroupHolder.addGroup(BatchingKeys.getBuildingSpriteGroup(player.getName()), spriteGroup);
    }


    protected void loadPlayerSpecificUnits(IAlliance alliance, TextureManager textureManager,
                                           VertexBufferObjectManager vboManager, IPlayer player) {
        TextureAtlas textureAtlas = alliance.loadUnitsToTexture(player.getName(), textureManager);
        SpriteGroup spriteGroup = new CleanableSpriteGroup(textureAtlas,
                mMovableUnitsLimit + mMovableUnitsBuffer, vboManager);
        SpriteGroupHolder.addGroup(BatchingKeys.getUnitSpriteGroup(player.getName()), spriteGroup);
        //unit pool
        player.createUnitPool(vboManager);
    }

    /** load images for bullets, health bars and player colors */
    private void loadBulletAndUnitHealthBar(
            TextureManager textureManager,
            VertexBufferObjectManager vertexBufferObjectManager) {
        BuildableBitmapTextureAtlas buildableTextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 128, 128);
        //player health bar
        PlayerHealthBar.loadResources(textureManager, vertexBufferObjectManager);
        IBitmapTextureAtlasSource atlasSource = AssetBitmapTextureAtlasSource.create(EaFallApplication
                .getContext().getAssets(), StringConstants.FILE_HEALTH_BAR_UNIT);
        //units health bar
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            ArgbColorComponentsSwapBitmapTextureAtlas textureSource =
                    new ArgbColorComponentsSwapBitmapTextureAtlas
                            (atlasSource,
                                    ORIGINAL_COLOR,
                                    ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart.createFromColor(
                                            player.getColor()));
            TextureRegionHolder.getInstance().addElement(
                    UnitHealthBar.getHealthBarTextureRegionKey(player.getName()),
                    BitmapTextureAtlasTextureRegionFactory.createFromSource(
                            buildableTextureAtlas, textureSource, false));
        }
        //bullets
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_ANNIHILATOR_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_HIGGSON_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_LASER_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_NEUTRINO_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_QUAKER_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_RAILGUN_BULLET,
                buildableTextureAtlas, EaFallApplication.getContext());
        //build
        boolean build = false;
        do {
            try {
                buildableTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
                        BitmapTextureAtlas>(1, 1, 1));
                build = true;
            } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
                //TODO is it possible?
                //TODO logger was here
            }
        } while (!build);
        buildableTextureAtlas.load();
        //sprite group
        SpriteGroup spriteGroup = new CleanableSpriteGroup(buildableTextureAtlas,
                mMovableUnitsLimit * 4, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.BULLET_AND_HEALTH, spriteGroup);
    }

    /** load units and planets explosions */
    private void loadExplosions(TextureManager textureManager,
                                VertexBufferObjectManager vertexBufferObjectManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, 1536, 1536);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.KEY_PLANET_EXPLOSION,
                textureAtlas, EaFallApplication.getContext(),
                0, 0, 4, 4);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.KEY_UNIT_EXPLOSION,
                textureAtlas, EaFallApplication.getContext(),
                SizeConstants.BETWEEN_TEXTURES_PADDING + SizeConstants.FILE_PLANET_DIAMETER * 4, 0,
                4, 4);
        textureAtlas.load();
        //sun + planets SpriteGroup
        SpriteGroup spriteGroup = new SpriteGroup(textureAtlas,
                mMovableUnitsLimit * 2, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.EXPLOSIONS, spriteGroup);
    }
}
