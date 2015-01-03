package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.shape.RectangularShape;
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

    /* holders */
    /** */
    protected List<Text> mDescriptionTextList;

    public BaseDescriptionAreaUpdater() {
        iniDescriptionTextList();
    }

    /** updates mDescriptionTextList */
    protected abstract void iniDescriptionTextList();

    /** attaches all of the mDescriptionTextList to given area */
    protected void attach(RectangularShape drawArea) {
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

    /** update description values (e.g. new building appear) */
    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId,
                                  final String raceName, final String teamName) {
        attach(drawArea);
    }

    /**
     * create description text with coordinates based on position. Abscissa always will be 0 and ordinate
     * is sum of the description font size and between lines break multiplied with position
     */
    protected DescriptionText createDescriptionText(int position, int stringId, VertexBufferObjectManager objectManager) {
        return createDescriptionText(0, position * (DescriptionText.sFontSize + mBetweenDescriptionLinesSpace),
                LocaleImpl.getInstance().getStringById(stringId), objectManager);
    }

    protected DescriptionText createDescriptionText(float x, float y, VertexBufferObjectManager objectManager) {
        return createDescriptionText(x, y, "", objectManager);
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

    protected Link createLink(float x, float y, VertexBufferObjectManager objectManager) {
        Link link = new Link(x + mSpace, y, objectManager);
        mDescriptionTextList.add(link);
        return link;
    }
}
