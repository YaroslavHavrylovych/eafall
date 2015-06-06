package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.MoneyText;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** particular resource loader */
public class ClientResourcesLoader extends BaseResourceLoader {
    public ClientResourcesLoader() {
    }

    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        //background
        loadBackgroundImage(StringConstants.FILE_BACKGROUND, textureManager);
        //alliance
        loadAllianceResources(textureManager);
        //players
        loadPlayerResources(textureManager, vertexBufferObjectManager);
        //bullets and health bars
        loadBulletAndHealthBar(textureManager, vertexBufferObjectManager);
        //sun and planets
        loadSunAndPlanets(textureManager, vertexBufferObjectManager);
        //other
        PopupManager.loadResource(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        MoneyText.loadFont(fontManager, textureManager);
        DescriptionPopupHud.loadFonts(fontManager, textureManager);
    }

    @Override
    public void unloadFonts(TextureManager textureManager, FontManager fontManager) {
        throw new UnsupportedOperationException("still not implemented");
    }

    @Override
    public void unloadImages() {
        throw new UnsupportedOperationException("still not implemented");
    }

    @Override
    public void addImage(String path, int width, int height) {
        throw new UnsupportedOperationException("no add image for the game");
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
            spriteGroup = new SpriteGroup(textureAtlas,
                    Config.getConfig().getMovableUnitsLimit(),
                    vertexBufferObjectManager);
            SpriteGroupHolder.addGroup(BatchingKeys.getUnitSpriteGroup(playerName), spriteGroup);
            //unit pool
            player.createUnitPool(vertexBufferObjectManager);
        }
    }

    /** load images for bullets, health bars and player colors */
    private void loadBulletAndHealthBar(
            TextureManager textureManager,
            VertexBufferObjectManager vertexBufferObjectManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.UNIT_SIZE,
                SizeConstants.BULLET_SIZE + SizeConstants.HEALTH_BAR_HEIGHT
                        + SizeConstants.BETWEEN_TEXTURES_PADDING, TextureOptions.BILINEAR);
        //load
        int y = 0;
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_HEALTH_BAR,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);
        y += SizeConstants.HEALTH_BAR_HEIGHT + SizeConstants.BETWEEN_TEXTURES_PADDING;
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_BULLET,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);
        smallObjectTexture.load();
        // health bar + 9 bullets at a time per unit, and 2 player (so * 2 in addition)
        SpriteGroup spriteGroup = new SpriteGroup(smallObjectTexture,
                Config.getConfig().getMovableUnitsLimit() * 10 * 2, vertexBufferObjectManager);
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
