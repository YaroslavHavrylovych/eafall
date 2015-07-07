package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.money.MoneyUpdatedEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import de.greenrobot.event.EventBus;

/**
 * Show money amount on the screen
 *
 * @author Yaroslav Havrylovych
 */
public class MoneyText extends Text {
    /** prefix which stand before money amount value */
    private String mMoneyValuePrefix;
    /** player, money of which will be displayed */
    private String mPlayerName;

    public MoneyText(String playerName, String prefix, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(200,
                SizeConstants.GAME_FIELD_HEIGHT - 90,
                FontHolder.getInstance().getElement(StringConstants.KEY_FONT_MONEY),
                generateTextString(prefix, 0), prefix.length() + 6, pVertexBufferObjectManager);
        mMoneyValuePrefix = prefix;
        mPlayerName = playerName;
        EventBus.getDefault().register(this);
    }

    private static String generateTextString(String prefix, int money) {
        return prefix + money;
    }

    public static void loadFont(FontManager fontManager, TextureManager textureManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolder.getInstance().addElement(StringConstants.KEY_FONT_MONEY, font);
        LoggerHelper.printDebugMessage(FontHolder.class.getCanonicalName(), "fonts loaded");
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(MoneyUpdatedEvent moneyUpdatedEvent) {
        if (!mPlayerName.equals(moneyUpdatedEvent.getPlayerName())) return;
        setText(generateTextString(mMoneyValuePrefix, moneyUpdatedEvent.getMoney()));
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final ShowUnitPathChooser showUnitPathChooser) {
        setVisible(false);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final HideUnitPathChooser hideUnitPathChooser) {
        setVisible(true);
    }
}
