package com.yaroslavlancelot.eafall.network.server.messages.constants;

public interface ServerMessagesConstants {
    short CONNECTION_CLOSE = 1;
    short WAITING_FOR_PLAYERS = 2;
    short GAME_START = 3;
    short STARTING_GAME = 4;
    short BUILDING_CREATED = 5;
    short BUILDING_UPGRADED = 6;
    short UNIT_CREATED = 7;
    short UNIT_MOVED = 8;
    short GAME_OBJECT_HEALTH_CHANGED = 9;
    short UNIT_FIRE = 10;
    short MONEY_CHANGED = 11;
    short GAME_STARTED = 12;
}
