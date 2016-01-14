package com.yaroslavlancelot.eafall.game.client.thick.income;

import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to display income images on the screen.
 *
 * @author Yaroslav Havrylovych
 */
public class ClientIncomeHandler extends SelfCleanable {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String INCOME_SOUND = "audio/sound/oxygen/income.ogg";
    private static ClientIncomeHandler INSTANCE;
    private final IPlayer mPlayer;
    private final Scene mScene;

    // ===========================================================
    // Fields
    // ===========================================================
    private IncomeButton mPlanetIncome;
    private List<IncomeListener> mIncomeListener;

    // ===========================================================
    // Constructors
    // ===========================================================
    private ClientIncomeHandler(IPlayer player, Scene scene, VertexBufferObjectManager vbo) {
        mScene = scene;
        mPlayer = player;
        PlanetStaticObject planet = player.getPlanet();
        mPlanetIncome = createIncomeButton(vbo);
        mPlanetIncome.setPosition(
                getPlanetIncomeX(planet.getX(), planet.getWidth()),
                getPlanetIncomeY(planet.getY(), planet.getHeight()));
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public static ClientIncomeHandler getIncomeHandler() {
        return INSTANCE;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void clear() {
        mPlanetIncome = null;
        INSTANCE = null;
        if (mIncomeListener != null) {
            mIncomeListener.clear();
            mIncomeListener = null;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * adds income listener
     *
     * @param incomeListener callbacks when income appears
     */
    public void registerIncomeListener(IncomeListener incomeListener) {
        if (mIncomeListener == null) {
            mIncomeListener = new ArrayList<>(1);
        }
        mIncomeListener.add(incomeListener);
    }

    /**
     * removes income listener
     *
     * @param incomeListener income listener to remove
     */
    public void unregisterIncomeListener(IncomeListener incomeListener) {
        mIncomeListener.remove(incomeListener);
    }

    /** Used to create income button */
    public ButtonSprite makeIncome(IncomeType incomeType, int money) {
        IncomeButton incomeButton;
        if (incomeType == IncomeType.PLANET) {
            incomeButton = mPlanetIncome;
        } else {
            incomeButton = createIncomeButton(mPlanetIncome.getVertexBufferObjectManager());
        }
        SoundFactory.getInstance().playSound(INCOME_SOUND);
        incomeButton.setMoney(money);
        incomeButton.resetAnimation();
        mScene.attachChild(incomeButton);
        mScene.registerTouchArea(incomeButton);
        if (mIncomeListener != null && mIncomeListener.size() > 0) {
            for (int i = 0; i < mIncomeListener.size(); i++) {
                mIncomeListener.get(i).onIncome(money, incomeType, incomeButton);
            }
        }
        return incomeButton;
    }

    private IncomeButton createIncomeButton(VertexBufferObjectManager vbo) {
        return new IncomeButton(0, 0, mPlayer, vbo) {
            @Override
            void timeout() {
                mScene.unregisterTouchArea(this);
            }
        };
    }

    /**
     * needed value
     *
     * @param planetY      planet abscissa
     * @param planetHeight planet width
     * @return the planet income image center abscissa
     */
    public static float getPlanetIncomeY(float planetY, float planetHeight) {
        float y = planetY;
        float yOffset = planetHeight / 2 + SizeConstants.INCOME_IMAGE_SIZE / 4;
        y += yOffset;
        return y;
    }

    /**
     * needed value
     *
     * @param planetX     planet abscissa
     * @param planetWidth planet width
     * @return the planet income image center abscissa
     */
    public static float getPlanetIncomeX(float planetX, float planetWidth) {
        float xOffset = planetWidth / 2 - SizeConstants.INCOME_IMAGE_SIZE / 2;
        if (PlanetStaticObject.isLeft(planetX)) {
            planetX += xOffset;
        } else {
            planetX -= xOffset;
        }
        return planetX;
    }

    public static void init(IPlayer player, Scene scene, VertexBufferObjectManager vbo) {
        INSTANCE = new ClientIncomeHandler(player, scene, vbo);
    }

    public static void loadImages(TextureManager textureManager) {
        IncomeButton.loadImages(textureManager);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /** Used for different income types */
    public enum IncomeType {
        /**
         * income out of the planet are spawned each
         * {@link com.yaroslavlancelot.eafall.game.events.periodic.money.MoneyUpdateCycle#MONEY_UPDATE_TIME}
         * second
         */
        PLANET,
        /** can be achieved by killing enemy units */
        UNIT
    }

    /** Income callback */
    public interface IncomeListener {
        void onIncome(int value, IncomeType incomeType, ButtonSprite button);
    }
}
