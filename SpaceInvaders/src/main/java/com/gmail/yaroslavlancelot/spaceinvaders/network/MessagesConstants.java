package com.gmail.yaroslavlancelot.spaceinvaders.network;

public interface MessagesConstants {
    short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = 1;
    short FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
    short FLAG_MESSAGE_SERVER_GAME_START = FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS + 1;
}
