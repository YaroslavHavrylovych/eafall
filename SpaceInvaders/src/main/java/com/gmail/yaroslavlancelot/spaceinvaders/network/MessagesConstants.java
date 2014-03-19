package com.gmail.yaroslavlancelot.spaceinvaders.network;

public interface MessagesConstants {
    short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = 1;
    short FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
    short FLAG_MESSAGE_SERVER_GAME_START = FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS + 1;
    short FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISHED = FLAG_MESSAGE_SERVER_GAME_START + 1;
    short FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING = FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISHED + 1;
    short FLAG_MESSAGE_SERVER_STARTING_GAME = FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING + 1;
    short FLAG_MESSAGE_GAME_LOADING_FINISHED = FLAG_MESSAGE_SERVER_STARTING_GAME + 1;
    short FLAG_MESSAGE_SERVER_BUILDING_CREATED = FLAG_MESSAGE_GAME_LOADING_FINISHED + 1;
    short FLAG_MESSAGE_SERVER_UNIT_CREATED = FLAG_MESSAGE_SERVER_BUILDING_CREATED + 1;
}
