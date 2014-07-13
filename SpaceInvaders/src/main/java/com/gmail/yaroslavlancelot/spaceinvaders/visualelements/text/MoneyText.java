package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.MoneyUpdatedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** Show money amount on the screen */
public class MoneyText extends Text {
    /** prefix which stand before money amount value */
    private String mMoneyValuePrefix;
    /** team, money of which will be displayed */
    private String mTeamName;

    //TODO with time float x should change to boolean (just left or right planet)
    public MoneyText(String teamName, String prefix, float x, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0f, SizeConstants.MONEY_FONT_SIZE * 2,
                FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                generateTextString(prefix, 0), prefix.length() + 6, pVertexBufferObjectManager);
        mMoneyValuePrefix = prefix;
        mTeamName = teamName;
        moveTextBaseOnPlanetPosition(x);
        EventBus.getDefault().register(this);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(MoneyUpdatedEvent moneyUpdatedEvent) {
        if (!mTeamName.equals(moneyUpdatedEvent.getTeamName())) return;
        setText(generateTextString(mMoneyValuePrefix, moneyUpdatedEvent.getMoney()));
    }

    @Override
    public void setText(CharSequence pText) throws OutOfCharactersException {
        super.setText(pText);
        if (getX() < SizeConstants.GAME_FIELD_WIDTH / 2)
            setX(SizeConstants.MONEY_PADDING);
        else
            setX(SizeConstants.GAME_FIELD_WIDTH - getWidth() - SizeConstants.MONEY_PADDING);
    }

    private static String generateTextString(String prefix, int money) {
        return prefix + money;
    }

    private void moveTextBaseOnPlanetPosition(float x) {
        if (x < SizeConstants.GAME_FIELD_WIDTH / 2) {
            setX(SizeConstants.MONEY_PADDING);
        } else {
            setX(SizeConstants.GAME_FIELD_WIDTH - getWidth() - SizeConstants.MONEY_PADDING);
        }
    }
}
