package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/** base operations for description area updaters */
public abstract class BaseDescriptionAreaUpdater implements IPopupUpdater.IDescriptionAreaUpdater {
    /* default values */
    /** space between different description lines */
    protected int mBetweenDescriptionLinesSpace = 5;
    /** space between description test and the link (e.g. unit health :(mSpace)health_value */
    protected int mSpace = 6;

    /* description text */
    /** text (links) to draw in the description area */
    protected List<Text> mDescriptionTextList;
    /** how much text lines in the description area */
    protected int mDescriptionTextLinesAmount;

    public BaseDescriptionAreaUpdater() {
        iniDescriptionTextList();
    }

    /** updates mDescriptionTextList */
    protected abstract void iniDescriptionTextList();

    /** update description values (e.g. new building appear) */
    @Override
    public void updateDescription(Shape drawArea, Object objectId,
                                  final String allianceName, final String teamName) {
        attach(drawArea);
    }

    /** attaches all of the mDescriptionTextList to given area */
    protected void attach(Shape drawArea) {
        if (mDescriptionTextList == null) return;
        for (Text text : mDescriptionTextList) {
            drawArea.attachChild(text);
        }
    }

    /** detaches all of the mDescriptionTextList (using detach self) */
    @Override
    public void clearDescription() {
        if (mDescriptionTextList == null) return;
        for (Text text : mDescriptionTextList) {
            text.detachSelf();
        }
    }

    /**
     * create description text with coordinates based on position. Abscissa always will be 0 and ordinate
     * is sum of the description font size and between lines break multiplied with position
     */
    protected DescriptionText createDescriptionText(int position, int stringId, VertexBufferObjectManager objectManager) {
        int fontHeight = DescriptionText.sFontSize + mBetweenDescriptionLinesSpace,
                halfFontHeight = fontHeight / 2;
        return createDescriptionText(0,
                SizeConstants.DESCRIPTION_POPUP_DESCRIPTION_AREA_HEIGHT -
                        halfFontHeight - position * fontHeight,
                LocaleImpl.getInstance().getStringById(stringId), objectManager);
    }

    protected DescriptionText createDescriptionText(float x, float y, String value, VertexBufferObjectManager objectManager) {
        DescriptionText text;
        if (value.isEmpty()) {
            text = new DescriptionText(x, y, objectManager);
        } else {
            text = new DescriptionText(x, y, value, objectManager);
        }
        mDescriptionTextList.add(text);
        return text;
    }

    protected DescriptionText createDescriptionText(float x, float y, VertexBufferObjectManager objectManager) {
        return createDescriptionText(x, y, "", objectManager);
    }

    protected Link createLink(float x, float y, VertexBufferObjectManager objectManager) {
        Link link = new Link(x + mSpace, y, objectManager);
        mDescriptionTextList.add(link);
        return link;
    }
}
