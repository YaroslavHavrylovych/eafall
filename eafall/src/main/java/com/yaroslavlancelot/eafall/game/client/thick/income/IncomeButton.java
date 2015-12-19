package com.yaroslavlancelot.eafall.game.client.thick.income;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

/**
 * Income button which appears on the screen when player income time comes or chance to
 * get income out of the dead unit.
 *
 * @author Yaroslav Havrylovych
 */
abstract class IncomeButton extends ButtonSprite {
    private final static float START_VALUE = .5f;
    private final static float END_VALUE = 1f;
    private final static int APPEARANCE_TIME = 5;
    private int mMoney;
    private float mCurrentValue = START_VALUE;
    private AlphaModifier mValueModifier = new AlphaModifier(APPEARANCE_TIME / 4, START_VALUE, END_VALUE);
    private TimerHandler mTimerHandler;

    IncomeButton(float pX, float pY, final IPlayer player,
                 VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY,
                (ITiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.FILE_INCOME),
                pVertexBufferObjectManager);
        //click
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pButtonSprite,
                                final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                player.changeMoney(mMoney);
                detachSelf();
            }
        });
        //blink
        initBlinking();
        //auto-remove
        mTimerHandler = new TimerHandler(APPEARANCE_TIME, false, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                setMoney(0);
                detachSelf();
                timeout();
            }
        });
        registerUpdateHandler(mTimerHandler);
    }

    void setMoney(int money) {
        mMoney = money;
    }

    private void initBlinking() {
        mValueModifier.addModifierListener(new IModifier.IModifierListener<IEntity>() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
                float endColor;
                if (mCurrentValue == END_VALUE) {
                    endColor = START_VALUE;
                } else {
                    endColor = END_VALUE;
                }
                mValueModifier.reset(mValueModifier.getDuration(), mCurrentValue, endColor);
                mCurrentValue = endColor;
            }
        });
        registerEntityModifier(mValueModifier);
    }

    void resetAnimation() {
        setAlpha(START_VALUE);
        mCurrentValue = END_VALUE; //as it will be after the modifier act
        mValueModifier.reset(mValueModifier.getDuration(), START_VALUE, END_VALUE);
        mTimerHandler.reset();
    }

    abstract void timeout();

    public static void loadImages(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        //background
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                2 * SizeConstants.INCOME_IMAGE_FILE_SIZE, SizeConstants.INCOME_IMAGE_FILE_SIZE,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(StringConstants.FILE_INCOME, textureAtlas,
                context, 0, 0, 2, 1);
        textureAtlas.load();
    }
}
