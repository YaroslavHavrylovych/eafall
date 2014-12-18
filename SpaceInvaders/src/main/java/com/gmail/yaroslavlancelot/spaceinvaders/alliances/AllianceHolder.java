package com.gmail.yaroslavlancelot.spaceinvaders.alliances;

import com.gmail.yaroslavlancelot.spaceinvaders.alliances.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.rebels.Rebels;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** you can get race instance from here */
public class AllianceHolder extends HolderUtils<IAlliance> {
    /** current class instance (singleton implementation) */
    private final static AllianceHolder sInstance = new AllianceHolder();

    private AllianceHolder() {
    }

    public static IAlliance getRace(String raceName) {
        return getInstance().getElement(raceName);
    }

    public static AllianceHolder getInstance() {
        return sInstance;
    }

    /**
     * invoke getInstance().addElement and add alliance based on alliance name in params.
     * If there is no alliance with the given name, then Imperials will be used as the alliance
     * for the given name.
     *
     * @param name            alliance name
     * @param objectManager   VertexBufferObjectManager
     * @param soundOperations SoundOperations
     */
    public static void addAllianceByName(String name,
                                         final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        IAlliance alliance;
        if (name.equals(Imperials.ALLIANCE_NAME)) {
            alliance = new Imperials(objectManager, soundOperations);
        } else if (name.equals(Rebels.ALLIANCE_NAME)) {
            alliance = new Rebels(objectManager, soundOperations);
        } else {
            alliance = new Imperials(objectManager, soundOperations);
        }

        getInstance().addElement(name, alliance);
    }
}
