package com.gmail.yaroslavlancelot.spaceinvaders.constants;

/**
 * Constant to define what type of team control will be used.
 */
public enum TeamControlBehaviourType {
    /*
     * SERVER SIDE
     */
    /** point that team controlled by user and it's server side */
    USER_SERVER_CONTROL,
    /** point that team controlled remotely and it's server side */
    REMOTE_SERVER_CONTROL,
    /** point that team control by bot (p.s. it's always on server side) */
    BOT_CONTROL,

    /*
     * CLIENT SIDE
     */
    /** point that team controlled by user and it's client side */
    USER_CLIENT_CONTROL,
    /** point that team controlled remotely and it's client side */
    REMOTE_CLIENT_CONTROL;
}
