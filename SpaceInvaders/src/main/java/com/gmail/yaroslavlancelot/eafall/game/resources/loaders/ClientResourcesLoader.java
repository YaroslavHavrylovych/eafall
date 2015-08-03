package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.PlayerHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.UnitHealthBar;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.scene.hud.EaFallHud;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Client resource loader (alliances, players etc).
 * <br/>
 * Resources which are used in the game.
 *
 * @author Yaroslav Havrylovych
 */
public class ClientResourcesLoader extends BaseResourceLoader {
    private int mMovableUnitsLimit = 200;

    public ClientResourcesLoader() {
        addImage(StringConstants.FILE_BACKGROUND,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
    }

    public void setMovableUnitsLimit(int movableUnitsLimit) {
        mMovableUnitsLimit = movableUnitsLimit;
    }

    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        //background
        loadBigImages(textureManager);
        //alliance
        loadAllianceResources(textureManager);
        //players
        loadPlayerResources(textureManager, vertexBufferObjectManager);
        //bullets and health bars
        loadBulletAndUnitHealthBar(textureManager, vertexBufferObjectManager);
        //sun and planets
        loadSunAndPlanets(textureManager, vertexBufferObjectManager);
        //oxygen, energy and time
        EaFallHud.loadResource(EaFallApplication.getContext(), textureManager);
        //other
        Context context = EaFallApplication.getContext();
        PopupManager.loadResource(context, textureManager);
        MenuPopupButton.loadResources(context, textureManager);
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        EaFallHud.loadFonts(fontManager, textureManager);
        DescriptionPopupHud.loadFonts(fontManager, textureManager);
    }

    @Override
    public void unloadFonts(final FontManager fontManager) {
        throw new UnsupportedOperationException("fonts unloading not supported in the game");
    }

    @Override
    public void unloadImages(final TextureManager textureManager) {
        throw new UnsupportedOperationException("resources unloading not supported in the game");
    }

    private void loadAllianceResources(TextureManager textureManager) {
        for (IAlliance alliance : AllianceHolder.getInstance().getElements()) {
            alliance.loadAllianceResources(textureManager);
        }
    }

    private void loadPlayerResources(TextureManager textureManager, VertexBufferObjectManager
            vertexBufferObjectManager) {
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            String playerName = player.getName();
            IAlliance alliance = player.getAlliance();
            //building SpriteGroup
            TextureAtlas textureAtlas = alliance.getBuildingTextureAtlas();
            SpriteGroup spriteGroup = new SpriteGroup(textureAtlas,
                    alliance.getBuildingsAmount(),
                    vertexBufferObjectManager);
            SpriteGroupHolder.addGroup(BatchingKeys.getBuildingSpriteGroup(playerName), spriteGroup);
            //unit SpriteGroup
            textureAtlas = alliance.loadUnitsToTexture(playerName, textureManager);
            spriteGroup = new SpriteGroup(textureAtlas, mMovableUnitsLimit, vertexBufferObjectManager);
            SpriteGroupHolder.addGroup(BatchingKeys.getUnitSpriteGroup(playerName), spriteGroup);
            //unit pool
            player.createUnitPool(vertexBufferObjectManager);
        }
    }

    /** load images for bullets, health bars and player colors */
    private void loadBulletAndUnitHealthBar(
            TextureManager textureManager,
            VertexBufferObjectManager vertexBufferObjectManager) {
        //player health bar
        PlayerHealthBar.loadResources(textureManager, vertexBufferObjectManager);
        //unit health bar
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                Math.max(SizeConstants.BULLET_SIZE, SizeConstants.UNIT_HEALTH_BAR_FILE_SIZE),
                SizeConstants.BULLET_SIZE
                        + 2 * SizeConstants.UNIT_HEALTH_BAR_FILE_SIZE
                        + 2 * SizeConstants.BETWEEN_TEXTURES_PADDING, TextureOptions.BILINEAR);
        int y = 0;
        IBitmapTextureAtlasSource atlasSource;
        int colorSize = SizeConstants.UNIT_HEALTH_BAR_FILE_SIZE;
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            atlasSource = createColoredTextureAtlasSource(player.getColor(),
                    colorSize, colorSize);
            TextureRegionHolder.addElementFromSource(
                    UnitHealthBar.getHealthBarTextureRegionKey(player.getName()),
                    smallObjectTexture, atlasSource, 0, y);
            y += colorSize + SizeConstants.BETWEEN_TEXTURES_PADDING;
        }
        //bullets
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_BULLET,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);

        smallObjectTexture.load();
        // health bar + 9 bullets at a time per unit, and 2 player (so * 2 in addition)
        //TODO check the situation when units doesn't have health bar
        SpriteGroup spriteGroup = new SpriteGroup(smallObjectTexture,
                mMovableUnitsLimit * 10 * 2, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.BULLET_AND_HEALTH, spriteGroup);
    }

    private void loadSunAndPlanets(TextureManager textureManager,
                                   VertexBufferObjectManager vertexBufferObjectManager) {
        int padding = SizeConstants.BETWEEN_TEXTURES_PADDING;
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
                Math.max(padding + 2 * SizeConstants.FILE_SUN_DIAMETER,
                        padding + 2 * SizeConstants.FILE_PLANET_DIAMETER),
                SizeConstants.FILE_SUN_DIAMETER
                        + SizeConstants.BETWEEN_TEXTURES_PADDING
                        + SizeConstants.FILE_PLANET_DIAMETER,
                TextureOptions.BILINEAR);
        //sun
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_SUN, 0, 0));
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SUN_HAZE,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_SUN_HAZE,
                        SizeConstants.FILE_SUN_DIAMETER + padding, 0));
        //planets
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_FIRST_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_FIRST_PLANET,
                        0, SizeConstants.FILE_SUN_DIAMETER + padding));
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SECOND_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_SECOND_PLANET,
                        SizeConstants.FILE_PLANET_DIAMETER + padding,
                        SizeConstants.FILE_SUN_DIAMETER + padding));

        atlas.load();
        //sun + planets SpriteGroup
        SpriteGroup spriteGroup = new SpriteGroup(atlas, 4, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.SUN_PLANET, spriteGroup);
    }
}
