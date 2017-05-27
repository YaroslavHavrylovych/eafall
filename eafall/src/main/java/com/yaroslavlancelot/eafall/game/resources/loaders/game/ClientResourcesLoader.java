package com.yaroslavlancelot.eafall.game.resources.loaders.game;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.audio.SoundOperations;
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.SuppressorSoundableAnimation;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.DescriptionPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;

import org.andengine.entity.sprite.batch.FlippableSpriteGroup;
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
    private String mSunPath;
    private String mSunHazePath;

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
        SuppressorSoundableAnimation.loadResources(textureManager);
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

    @Override
    public void loadSounds(final SoundOperations soundOperations) {
        super.loadSounds(soundOperations);
        soundOperations.loadSound(ClientIncomeHandler.INCOME_SOUND, 100);
        soundOperations.loadSound(PlanetStaticObject.PLANET_EXPLOSION_SOUND, 200);
        soundOperations.loadSound(SuppressorSoundableAnimation.SOUND, 200);
        soundOperations.loadSound(StringConstants.SOUND_CLOCK_TICK_PATH, 50);
    }

    public void setSunPath(String sunPath, String sunHazePath) {
        mSunHazePath = sunHazePath;
        mSunPath = sunPath;
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
        if(mSunPath != null) {
            TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SUN,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                            atlas, EaFallApplication.getContext(),
                            mSunPath, 0, 0));
            TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SUN_HAZE,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                            atlas, EaFallApplication.getContext(),
                            mSunHazePath,
                            SizeConstants.FILE_SUN_DIAMETER + padding, 0));
        }
        //planets
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_FIRST_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.PLANET,
                        0, SizeConstants.FILE_SUN_DIAMETER + padding));
        TextureRegionHolder.getInstance().addElement(StringConstants.KEY_SECOND_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        atlas, EaFallApplication.getContext(),
                        StringConstants.PLANET,
                        SizeConstants.FILE_PLANET_DIAMETER + padding,
                        SizeConstants.FILE_SUN_DIAMETER + padding));

        atlas.load();
        //sun + planets SpriteGroup
        SpriteGroup spriteGroup = new FlippableSpriteGroup(atlas, 4, vertexBufferObjectManager);
        SpriteGroupHolder.addGroup(BatchingKeys.SUN_PLANET, spriteGroup);
    }
}
