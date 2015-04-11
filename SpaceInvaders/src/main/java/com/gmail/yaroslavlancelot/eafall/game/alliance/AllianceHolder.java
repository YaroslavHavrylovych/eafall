package com.gmail.yaroslavlancelot.eafall.game.alliance;

import com.gmail.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.gmail.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.gmail.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.gmail.yaroslavlancelot.eafall.general.Holder;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** you can get race instance from here */
public class AllianceHolder extends Holder<IAlliance> {
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
                                         final VertexBufferObjectManager objectManager, 
                                         final SoundOperations soundOperations) {
        IAlliance alliance;
        if (name.equals(Imperials.ALLIANCE_NAME)) {
            alliance = new Imperials(objectManager, soundOperations);
        } else if (name.equals(Rebels.ALLIANCE_NAME)) {
            alliance = new Rebels(objectManager, soundOperations);
        } else if (name.equals(Mutants.ALLIANCE_NAME)) {
            alliance = new Mutants(objectManager, soundOperations);
        } else {
            alliance = new Imperials(objectManager, soundOperations);
        }

        getInstance().addElement(name, alliance);
    }
}
