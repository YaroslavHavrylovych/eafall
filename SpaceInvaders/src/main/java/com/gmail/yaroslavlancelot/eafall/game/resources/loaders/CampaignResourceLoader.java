package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Used to load resource for guide campaign (the campaign home page)
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignResourceLoader extends BaseResourceLoader {
    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        //background
        loadBackgroundImage(textureManager);
        //images
        loadImagesList(textureManager);
        //button
        TextButton.loadResources(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        //button
        TextButton.loadFonts(fontManager, textureManager);
    }

    @Override
    public void unloadFonts(TextureManager textureManager, FontManager fontManager) {
    }

    @Override
    public void unloadImages() {
    }

    private void loadImagesList(TextureManager textureManager) {
        int imagesAmount = mImagesList.size();
        int atlases = imagesAmount % 4 == 0 ? imagesAmount / 4 : imagesAmount / 4 + 1;
        int imageSize = 400;
        int width, height = width = imageSize * 4;
        int atlasPosition;
        int position = 0;
        end:
        for (int i = 0; i < atlases; i++) {
            BitmapTextureAtlas textureAtlas =
                    new BitmapTextureAtlas(textureManager, width, height, TextureOptions.BILINEAR);
            for (atlasPosition = 0; atlasPosition < 4; atlasPosition++) {
                if (position >= imagesAmount) {
                    if (atlasPosition != 0) {
                        textureAtlas.load();
                    }
                    break end;
                }
                TextureRegionHolder.
                        addElementFromAssets(mImagesList.get(position++),
                                textureAtlas, EaFallApplication.getContext(),
                                (atlasPosition % 2) * imageSize, (atlasPosition / 2) * imageSize);
            }
            textureAtlas.load();
        }
    }
}
