package com.gmail.yaroslavlancelot.eafall.game.entity.health;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.engine.AlphaToChildrenSpriteGroup;
import com.gmail.yaroslavlancelot.eafall.game.engine.ArgbColorComponentsSwapBitmapTextureAtlas;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent player health bar (same as planet health).
 * This health bar represent the health as some amount of separated
 * health elements (we call it <b>planks</b>). Each plank represent particular health diapason.
 * Plank alpha simplified to understand particular plank state.
 * <br/>
 * {@link PlayerHealthBar#loadResources(TextureManager, VertexBufferObjectManager)}
 * loads health bar for each player (diff in color) and health bar
 * carcass/template/(background sprite) which one for both.
 *
 * @author Yaroslav Havrylovych
 */
public class PlayerHealthBar implements IHealthBar {
    private static final String TAG = PlayerHealthBar.class.getCanonicalName();
    /** maximum health planks amount */
    private static final int HEALTH_PLANKS_AMOUNT = 6;
    /** 2 players + background sprite */
    private static final int GROUP_CAPACITY = 2 * HEALTH_PLANKS_AMOUNT + 1;
    /*
     * used as constants for calculations
     */
    /** each next plank starting abscissa can be found as previous abscissa + this constant */
    private static final int BETWEEN_PLANK_DISTANCE = 25;
    /** first plank abscissa */
    private static final int FIRST_PLANK_BOTTOM_LEFT_ABSCISSA = 44;
    /** plank bottom left ordinate (same for each) */
    private static final int PLANK_BOTTOM_LEFT_ORDINATE = 4;
    /** health bar background sprite half width */
    private static final int HEALTH_BAR_CARCASS_HALF_WIDTH = 262;
    /*
     * other stuff
     */
    /** original image contains this argb component */
    private static final ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart
            ORIGINAL_COLOR = ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart.B;
    /** list of health bar planks */
    private final List<BatchedSprite> mHealthPlanks = new ArrayList<>(HEALTH_PLANKS_AMOUNT);

    public PlayerHealthBar(String player, VertexBufferObjectManager vertexBufferObjectManager,
                           boolean left) {
        //create health bar sprite
        BatchedSprite batchedSprite;
        for (int i = 0; i < HEALTH_PLANKS_AMOUNT; i++) {
            batchedSprite = new BatchedSprite(0, 0, TextureRegionHolder
                    .getRegion(generateHealthItemKey(player, i)), vertexBufferObjectManager);
            batchedSprite.setSpriteGroupName(BatchingKeys.PLAYER_HEALTH);
            setPlankPosition(batchedSprite, i, left);
            if (!left) {
                //hard trick to flip entity
                batchedSprite.setWidth(-batchedSprite.getWidth());
            }
            mHealthPlanks.add(batchedSprite);
        }
    }

    @Override
    public void attachHealthBar(final IEntity parent) {
        for (IEntity healthBarPlank : mHealthPlanks) {
            parent.attachChild(healthBarPlank);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        //no need to change position
    }

    @Override
    public void redrawHealthBar(int healthMax, int actualHealth) {
        int onePlankHealth = healthMax / HEALTH_PLANKS_AMOUNT;
        IEntity healthPlankEntity;
        for (int healthPlank = 1; healthPlank <= HEALTH_PLANKS_AMOUNT; healthPlank++) {
            healthPlankEntity = mHealthPlanks.get(healthPlank - 1);
            int missHealth = healthMax - actualHealth;
            float plankValue = onePlankHealth * healthPlank;
            float plankHealthDiff = plankValue - missHealth;
            boolean visible = plankHealthDiff > 0;
            healthPlankEntity.setVisible(visible);
            if (visible) {
                if (plankHealthDiff < onePlankHealth) {
                    //as more currentWithPlankHealthDifference as biggest alpha(0,0..1,0) has to be set
                    healthPlankEntity.setAlpha(plankHealthDiff / onePlankHealth);
                } else {
                    healthPlankEntity.setAlpha(1f);
                }
            }
        }
    }

    public static void loadResources(final TextureManager textureManager,
                                     VertexBufferObjectManager objectManager) {
        BuildableBitmapTextureAtlas textureAtlas = new BuildableBitmapTextureAtlas(textureManager,
                550, 512);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_HEALTH_BAR_CARCASS,
                textureAtlas, EaFallApplication.getContext());
        IBitmapTextureAtlasSource source;
        ArgbColorComponentsSwapBitmapTextureAtlas swappedColorsSource;
        for (int i = 0; i < HEALTH_PLANKS_AMOUNT; i++) {
            source = AssetBitmapTextureAtlasSource.create(EaFallApplication
                    .getContext().getAssets(), pathToHealthItem(i + 1));
            for (IPlayer player : PlayersHolder.getInstance().getElements()) {
                swappedColorsSource = new ArgbColorComponentsSwapBitmapTextureAtlas(
                        source.deepCopy(),
                        ORIGINAL_COLOR,
                        ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart.createFromColor(
                                player.getColor()));
                TextureRegionHolder.getInstance().addElement(
                        generateHealthItemKey(player.getName(), i),
                        BitmapTextureAtlasTextureRegionFactory.createFromSource(
                                textureAtlas, swappedColorsSource, false));
            }
        }

        boolean build = false;
        do {
            try {
                textureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
                        BitmapTextureAtlas>(1, 1, 1));
                build = true;
            } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
                //TODO is it possible?
                LoggerHelper.printErrorMessage(TAG,
                        "failed to build texture atlas for player health");
            }
        } while (!build);
        textureAtlas.load();
        SpriteGroup spriteGroup = new AlphaToChildrenSpriteGroup(textureAtlas, GROUP_CAPACITY, objectManager);
        spriteGroup.setTag(BatchingKeys.BatchTag.GAME_HUD.value());
        SpriteGroupHolder.addGroup(BatchingKeys.PLAYER_HEALTH, spriteGroup);
    }

    private static String generateHealthItemKey(String player, int id) {
        return player + "_health_item_" + id;
    }

    private static String pathToHealthItem(int id) {
        return StringConstants.FILE_HEALTH_BAR_PLAYER + id + ".png";
    }

    protected void setPlankPosition(IEntity healthBarPlank, int id,
                                    boolean leftPlanet) {
        int plankCenterX = FIRST_PLANK_BOTTOM_LEFT_ABSCISSA + (id * BETWEEN_PLANK_DISTANCE)
                + (int) healthBarPlank.getWidth() / 2;
        int plankCenterY = PLANK_BOTTOM_LEFT_ORDINATE + (int) healthBarPlank.getHeight() / 2;

        plankCenterX = leftPlanet
                ? SizeConstants.HALF_FIELD_WIDTH - HEALTH_BAR_CARCASS_HALF_WIDTH + plankCenterX
                : SizeConstants.HALF_FIELD_WIDTH + HEALTH_BAR_CARCASS_HALF_WIDTH - plankCenterX;
        plankCenterY = SizeConstants.GAME_FIELD_HEIGHT - plankCenterY;
        healthBarPlank.setPosition(plankCenterX, plankCenterY);
    }

    @Override
    public void setVisible(boolean visible) {
        // just a stub to fit interface
    }

    @Override
    public boolean isVisible() {
        // just a stub to fit interface
        return true;
    }
}
