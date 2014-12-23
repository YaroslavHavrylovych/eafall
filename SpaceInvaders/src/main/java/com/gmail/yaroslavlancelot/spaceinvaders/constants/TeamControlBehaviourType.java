package com.gmail.yaroslavlancelot.spaceinvaders.constants;

/**
 * Constant to define what type of team control will be used.
 */
public enum TeamControlBehaviourType {
    /*
     * SERVER SIDE
     */
    /** point that team controlled by user and it's server side */
    USER_CONTROL_ON_SERVER_SIDE,
    /** point that team controlled remotely and it's server side */
    REMOTE_CONTROL_ON_SERVER_SIDE,
    /** point that team control by bot (p.s. it's always on server side) */
    BOT_CONTROL_ON_SERVER_SIDE,

    /*
     * CLIENT SIDE
     */
    /** point that team controlled by user and it's client side */
    USER_CONTROL_ON_CLIENT_SIDE,
    /** point that team controlled remotely and it's client side */
    REMOTE_CONTROL_ON_CLIENT_SIDE;

    /** returns true if type parameter is team type controlled by user (remote or local) */
    public static boolean isUserControlType(TeamControlBehaviourType type) {
        return type == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE || type == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE;
    }


    /** return true if type parameter corresponding to client side */
    public static boolean isClientSide(TeamControlBehaviourType type) {
        //TODO check this when you will check the multiplayer
        return type == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE || type == TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE;
    }
}
