package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * @author Yaroslav Havrylovych
 */
public class MovableUnitsLimitText extends Text {
    /** units amount text on the screen template (e.g. cur_amount/max_amount) */
    private static final String sTextTemplate = "%d/"
            + String.format("%d", Config.getConfig().getMovableUnitsLimit());

    public MovableUnitsLimitText(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, FontHolder.getInstance().getElement(StringConstants.KEY_FONT_MONEY),
                String.format(sTextTemplate, 0),
                Integer.toString(Config.getConfig().getMovableUnitsLimit()).length() * 2 + 1,
                vertexBufferObjectManager);
        EventBus.getDefault().register(this);
    }

    public synchronized void setValue(int value) {
        setText(String.format(sTextTemplate, value));
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
