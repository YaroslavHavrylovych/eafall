package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.PlanetStaticObject;
import org.andengine.entity.sprite.Sprite;

import java.util.List;

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

    @SuppressWarnings("unused")
    List<ITeam> getFriendlyTeams();

    @SuppressWarnings("unused")
    List<Sprite> getTeamUnits();

    PlanetStaticObject getTeamPlanet();

    String getTeamName();
}
