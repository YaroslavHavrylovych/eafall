package com.yaroslavlancelot.eafall.game.resources.loaders.campaign;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.campaign.visual.CampaignTitleText;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.resources.BaseResourceLoader;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to load resources for guide campaign (the campaign home page)
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignResourceLoader extends BaseResourceLoader {
    private List<TextureAtlas> mAtlases = new ArrayList<>(5);

    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        super.loadImages(textureManager, vertexBufferObjectManager);
        //background
        mAtlases.addAll(loadBigImages(textureManager));
        //images
        mAtlases.add(loadSmallImages(textureManager));
        //button
        mAtlases.add(TextButton.loadResources(EaFallApplication.getContext(), textureManager));
        //foreground
        mAtlases.add(loadForeground(textureManager, EaFallApplication.getContext()));
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        super.loadFonts(textureManager, fontManager);
        CampaignTitleText.loadFonts(fontManager, textureManager);
    }

    private TextureAtlas loadForeground(TextureManager textureManager, Context context) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager, 1920, 1080, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_CAMPAIGN_HUD_FOREGROUND, atlas, context, 0, 0);
        atlas.load();
        return atlas;
    }

    @Override
    public void unloadFonts(final FontManager fontManager) {
        throw new UnsupportedOperationException("fonts unloading not supported in campaign");
    }

    @Override
    public void unloadImages(final TextureManager textureManager) {
        for (TextureAtlas textureAtlas : mAtlases) {
            textureAtlas.unload();
        }
    }
}
