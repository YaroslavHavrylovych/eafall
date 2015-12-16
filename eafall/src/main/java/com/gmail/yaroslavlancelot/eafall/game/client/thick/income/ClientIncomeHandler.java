package com.gmail.yaroslavlancelot.eafall.game.client.thick.income;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Used to display income images on the screen.
 *
 * @author Yaroslav Havrylovych
 */
public class ClientIncomeHandler extends SelfCleanable {
    // ===========================================================
    // Constants
    // ===========================================================
    private static ClientIncomeHandler INSTANCE;
    private final IPlayer mPlayer;
    private final Scene mScene;

    // ===========================================================
    // Fields
    // ===========================================================
    private IncomeButton mPlanetIncome;

    // ===========================================================
    // Constructors
    // ===========================================================
    private ClientIncomeHandler(IPlayer player, Scene scene, VertexBufferObjectManager vbo) {
        mScene = scene;
        mPlayer = player;
        PlanetStaticObject planet = player.getPlanet();
        float x = planet.getX(), y = planet.getY();
        float xOffset = planet.getWidth() / 2 + SizeConstants.INCOME_IMAGE_SIZE / 4,
                yOffset = planet.getHeight() / 2 + SizeConstants.INCOME_IMAGE_SIZE / 4;
        y += yOffset;
        if (planet.isLeft()) {
            x += xOffset;
        } else {
            x -= xOffset;
        }
        mPlanetIncome = createIncomeButton(vbo);
        mPlanetIncome.setPosition(x, y);
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
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public ButtonSprite makeIncome(IncomeType incomeType, int money) {
        IncomeButton incomeButton;
        if (incomeType == IncomeType.PLANET) {
            incomeButton = mPlanetIncome;
        } else {
            incomeButton = createIncomeButton(mPlanetIncome.getVertexBufferObjectManager());
        }
        incomeButton.setMoney(money);
        incomeButton.resetAnimation();
        mScene.attachChild(incomeButton);
        mScene.registerTouchArea(incomeButton);
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
         * {@link com.gmail.yaroslavlancelot.eafall.game.events.periodic.money.MoneyUpdateCycle#MONEY_UPDATE_TIME}
         * second
         */
        PLANET,
        /** can be achieved by killing enemy units */
        UNIT
    }
}
