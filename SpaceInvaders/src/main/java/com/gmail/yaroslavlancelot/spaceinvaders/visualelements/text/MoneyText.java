package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.MoneyUpdatedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.ShowUnitPathChooser;
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

    public MoneyText(String teamName, String prefix, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, SizeConstants.MONEY_FONT_SIZE * 2,
                FontHolderUtils.getInstance().getElement(StringsAndPathUtils.KEY_FONT_MONEY),
                generateTextString(prefix, 0), prefix.length() + 6, pVertexBufferObjectManager);
        mMoneyValuePrefix = prefix;
        mTeamName = teamName;
        setX(SizeConstants.GAME_FIELD_WIDTH / 2 - getWidth() / 2);
        EventBus.getDefault().register(this);
    }

    private static String generateTextString(String prefix, int money) {
        return prefix + money;
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
        setX(SizeConstants.GAME_FIELD_WIDTH / 2 - getWidth() / 2);
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
