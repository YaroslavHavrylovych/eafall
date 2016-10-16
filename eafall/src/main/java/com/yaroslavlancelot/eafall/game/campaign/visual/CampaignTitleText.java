package com.yaroslavlancelot.eafall.game.campaign.visual;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.game.visual.text.RecenterText;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import timber.log.Timber;

/** text used in description popup */
public class CampaignTitleText extends RecenterText {
    public final static int sFontSize = SizeConstants.CAMPAIGN_TITLE_TEXT;
    public final static String sFontKey = "campaign_title_text_font_key";

    public CampaignTitleText(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, "*", vertexBufferObjectManager);
    }

    public CampaignTitleText(float x, float y, CharSequence text, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(sFontKey), text,
                text.length() < 30 ? 30 : text.length(), vertexBufferObjectManager);
        setWidth(FontUtils.measureText(FontHolder.getInstance().getElement(sFontKey), text) + 15);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        Timber.v("Campaign title font loaded");
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 2048);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                sFontSize, true, android.graphics.Color.argb(255, 255, 255, 255));
        font.load();
        FontHolder.getInstance().addElement(sFontKey, font);
    }
}
