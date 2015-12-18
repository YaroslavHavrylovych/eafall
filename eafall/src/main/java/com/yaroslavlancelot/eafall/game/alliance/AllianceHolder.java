package com.yaroslavlancelot.eafall.game.alliance;

import com.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.yaroslavlancelot.eafall.general.Holder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** you can get alliance instance from here */
public class AllianceHolder extends Holder<IAlliance> {
    /** current class instance (singleton implementation) */
    private final static AllianceHolder sInstance = new AllianceHolder();

    private AllianceHolder() {
    }

    public static AllianceHolder getInstance() {
        return sInstance;
    }

    public static IAlliance getAlliance(String allianceName) {
        return getInstance().getElement(allianceName);
    }

    /**
     * invoke getInstance().addElement and add alliance based on alliance name in params.
     * If there is no alliance with the given name, then Imperials will be used as the alliance
     * for the given name.
     *
     * @param name          alliance name
     * @param objectManager VertexBufferObjectManager
     */
    public static void addAllianceByName(String name,
                                         final VertexBufferObjectManager objectManager) {
        IAlliance alliance = getAlliance(name);
        //if alliance is already loaded then no need to load the data again
        if (alliance != null) {
            return;
        }
        if (name.equals(Imperials.ALLIANCE_NAME)) {
            alliance = new Imperials(objectManager);
        } else if (name.equals(Rebels.ALLIANCE_NAME)) {
            alliance = new Rebels(objectManager);
        } else if (name.equals(Mutants.ALLIANCE_NAME)) {
            alliance = new Mutants(objectManager);
        } else {
            throw new IllegalArgumentException("unknown alliance wants to rule the world (sent as param)");
        }

        getInstance().addElement(name, alliance);
    }
}
