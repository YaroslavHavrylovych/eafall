package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import org.andengine.entity.sprite.Sprite;

/**  */
public interface ITeam {
    void addObjectToTeam(Sprite sprite);

    @SuppressWarnings("unused")
    void removeObjectFromTeam(Sprite sprite);

    @SuppressWarnings("unused")
    boolean isObjectInTeam(Sprite sprite);

    void addFriendlyTeam(ITeam iTeam);

    @SuppressWarnings("unused")
    void removeFriendlyTeam(ITeam iTeam);

    boolean isFriendlyTeam(ITeam iTeam);

    PlanetStaticObject getTeamPlanet();
}
