package com.gmail.yaroslavlancelot.eafall.game.alliance;

import com.gmail.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.gmail.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.gmail.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.gmail.yaroslavlancelot.eafall.general.Holder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** you can get alliance instance from here */
public class AllianceHolder extends Holder<IAlliance> {
    /** current class instance (singleton implementation) */
    private final static AllianceHolder sInstance = new AllianceHolder();

    private AllianceHolder() {
    }

    public static IAlliance getAlliance(String allianceName) {
        return getInstance().getElement(allianceName);
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
     */
    public static void addAllianceByName(String name,
                                         final VertexBufferObjectManager objectManager) {
        IAlliance alliance;
        if (name.equals(Imperials.ALLIANCE_NAME)) {
            alliance = new Imperials(objectManager);
        } else if (name.equals(Rebels.ALLIANCE_NAME)) {
            alliance = new Rebels(objectManager);
        } else if (name.equals(Mutants.ALLIANCE_NAME)) {
            alliance = new Mutants(objectManager);
        } else {
            alliance = new Imperials(objectManager);
        }

        getInstance().addElement(name, alliance);
    }
}
