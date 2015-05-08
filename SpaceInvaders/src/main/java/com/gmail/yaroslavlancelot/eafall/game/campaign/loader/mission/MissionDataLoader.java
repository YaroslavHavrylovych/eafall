package com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Used to load mission information
 *
 * @author Yaroslav Havrylovych
 */
@Root(name = "mission")
public class MissionDataLoader {
    @Element(name = "config")
    public String config;

    @Element(name = "definition")
    public DefinitionLoader definition;
}
