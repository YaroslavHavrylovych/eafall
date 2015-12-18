package com.gmail.yaroslavlancelot.eafall.game.resources.loaders.game;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.DescriptionPopup;
import com.gmail.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Client resource loader (alliances, players etc).
 * <br/>
 * Resources which are used in the game.
 *
 * @author Yaroslav Havrylovych
 */
public class ClientResourcesLoader extends BaseGameObjectsLoader {
    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        super.loadImages(textureManager, vertexBufferObjectManager);
        //sun and planets
        loadSunAndPlanets(textureManager, vertexBufferObjectManager);
        //oxygen, energy and time
        ClientGameHud.loadResource(EaFallApplication.getContext(), textureManager);
        //other
        Context context = EaFallApplication.getContext();
        ConstructionsPopup.loadResource(context, textureManager);
        DescriptionPopup.loadResources(context, textureManager);
        ClientIncomeHandler.loadImages(textureManager);
    }

    @Override
    public void loadFonts(final TextureManager textureManager, final FontManager fontManager) {
        super.loadFonts(textureManager, fontManager);
        DescriptionPopup.loadFonts(fontManager, textureManager);
        GameOverPopup.loadFonts(fontManager, textureManager);
    }

    @Override
    protected void loadAllianceSpecificResources(TextureManager textureManager) {
        for (IAlliance alliance : AllianceHolder.getInstance().getElements()) {
            alliance.loadBuildings(textureManager);
            alliance.loadUnits(textureManager);
        }
    }

    @Override
    protected void loadPlayerSpecificResources(TextureManager textureManager,
                                               VertexBufferObjectManager vboManager) {
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            IAlliance alliance = player.getAlliance();
            loadPlayerSpecificBuildings(alliance, vboManager, player);
            loadPlayerSpecificUnits(alliance, textureManager, vboManager, player);
        }
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
