package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

/**
 * mark that particular {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject}
 * assigned to some team (TeamObject is just short for TeamGameObject)
 */
public interface ITeamObject {
    /** return the name of the team to which this object is assigned to */
    String getTeam();

    /** set team name which enough to retrieve team out of the holder */
    void setTeam(String teamName);
}
