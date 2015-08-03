package com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Yaroslav Havrylovych
 */
@Root(name = "definition")
public class DefinitionLoader {
    @Element(name = "time_limit", required = false)
    public Integer time_limit;

    @Element(name = "type")
    public String type;

    @Element(name = "value", required = false)
    public String value;
}
