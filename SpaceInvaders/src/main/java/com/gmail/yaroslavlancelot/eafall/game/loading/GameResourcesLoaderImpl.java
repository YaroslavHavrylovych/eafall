package com.gmail.yaroslavlancelot.eafall.game.loading;

import android.graphics.Typeface;

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
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/** particular resource loader */
public class GameResourcesLoaderImpl implements GameResourceLoader {
    GameResourcesLoaderImpl() {
    }

    @Override
    public void loadSplashImages(TextureManager textureManager,
                                 VertexBufferObjectManager vertexBufferObjectManager) {
        SplashScene.loadResources(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public void loadInGameImages(TextureManager textureManager,
                                 VertexBufferObjectManager vertexBufferObjectManager) {
        //background
        loadBackground(textureManager);
        //alliance
        loadAllianceResources(textureManager);
        //players
        loadTeamResources(vertexBufferObjectManager);
        //bullets and health bars
        loadBulletAndHealthBarAndTeamColor(textureManager, vertexBufferObjectManager);
        //sun and planets
        loadSunAndPlanets(textureManager, vertexBufferObjectManager);
        //other
        PopupManager.loadResource(EaFallApplication.getContext(), textureManager);
    }

    /** loads game scene background */
    public static void loadBackground(TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_BACKGROUND,
                smallObjectTexture, EaFallApplication.getContext(), 0, 0);
        smallObjectTexture.load();
    }

    private void loadAllianceResources(TextureManager textureManager) {
        for (IAlliance alliance : AllianceHolder.getInstance().getElements()) {
            alliance.loadResources(textureManager);
        }
    }

    private void loadTeamResources(VertexBufferObjectManager vertexBufferObjectManager) {
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            String teamName = team.getName();
            IAlliance alliance = team.getAlliance();
            //building
            TextureAtlas textureAtlas = alliance.getBuildingTextureAtlas();
            SpriteGroup spriteGroup = new SpriteGroup(textureAtlas,
                    alliance.getBuildingsAmount(),
                    vertexBufferObjectManager);
            SpriteGroupHolder.addGroup(BatchingKeys.getBuildingSpriteGroup(teamName), spriteGroup);
            //unit
            textureAtlas = alliance.getUnitTextureAtlas();
            spriteGroup = new SpriteGroup(textureAtlas,
                    Config.getConfig().getMovableUnitsLimit(),
                    vertexBufferObjectManager);
            SpriteGroupHolder.addGroup(BatchingKeys.getUnitSpriteGroup(teamName), spriteGroup);
        }
    }

    @Override
    public void loadProfilingFonts(TextureManager textureManager, FontManager fontManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolder.getInstance().addElement("profiling", font);
    }

    /** load images for bullets, health bars and team colors */
    private void loadBulletAndHealthBarAndTeamColor(
            TextureManager textureManager,
            VertexBufferObjectManager vertexBufferObjectManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.UNIT_SIZE,
                SizeConstants.BULLET_SIZE + SizeConstants.HEALTH_BAR_HEIGHT
                        + SizeConstants.TEAM_COLOR_AREA_SIZE
                        + 2 * SizeConstants.BETWEEN_TEXTURES_PADDING, TextureOptions.BILINEAR);
        //load
        int y = 0;
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_HEALTH_BAR,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);
        y += SizeConstants.HEALTH_BAR_HEIGHT + SizeConstants.BETWEEN_TEXTURES_PADDING;
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_BULLET,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);
        y += SizeConstants.BULLET_SIZE + SizeConstants.BETWEEN_TEXTURES_PADDING;
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_TEAM_COLOR,
                smallObjectTexture, EaFallApplication.getContext(), 0, y);
        smallObjectTexture.load();
        // health bar + 9 bullets at a time per unit, and 2 player (so * 2 in addition)
        SpriteGroup spriteGroup = new SpriteGroup(smallObjectTexture,
                Config.getConfig().getMovableUnitsLimit() * 10 * 2, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.BULLET_HEALTH_TEAM_COLOR, spriteGroup);
    }

    private void loadSunAndPlanets(TextureManager textureManager,
                                   VertexBufferObjectManager vertexBufferObjectManager) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.BETWEEN_TEXTURES_PADDING
                        + 2 * SizeConstants.PLANET_DIAMETER,
                SizeConstants.FILE_SUN_DIAMETER
                        + SizeConstants.BETWEEN_TEXTURES_PADDING
                        + SizeConstants.PLANET_DIAMETER,
                TextureOptions.BILINEAR);

        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_SUN, 0, 0));
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_FIRST_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_FIRST_PLANET,
                        0,
                        SizeConstants.FILE_SUN_DIAMETER + SizeConstants.BETWEEN_TEXTURES_PADDING));
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SECOND_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.FILE_SECOND_PLANET,
                        SizeConstants.PLANET_DIAMETER + SizeConstants.BETWEEN_TEXTURES_PADDING,
                        SizeConstants.FILE_SUN_DIAMETER + SizeConstants.BETWEEN_TEXTURES_PADDING));

        atlas.load();
        //sun + planets SpriteGroup
        SpriteGroup spriteGroup = new SpriteGroup(atlas, 3, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.SUN_PLANET, spriteGroup);
    }

    @Override
    public void loadInGameFonts(TextureManager textureManager, FontManager fontManager) {
        FontHolder.loadGeneralGameFonts(fontManager, textureManager);
        DescriptionPopupHud.loadFonts(fontManager, textureManager);
    }

    @Override
    public void unloadInGameFonts(TextureManager textureManager, FontManager fontManager) {

    }

    @Override
    public void unloadGameImages() {

    }

    @Override
    public void unloadSplashImages() {

    }
}
