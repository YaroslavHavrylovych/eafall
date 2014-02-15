package com.gmail.yaroslavlancelot.spaceinvaders.network;

import org.andengine.extension.multiplayer.protocol.server.SocketServer;

/**
 * Contains connection to server which in static field so you can retrieve without creating class instance.
 */
public class SocketServerHolder {
    /** connector to server which can be null */
    private static volatile SocketServer sSocketServer;

    /**
     * @return existing connector to server or null
     */
    public static SocketServer getSocketServer() {
        return sSocketServer;
    }

    /**
     * set new server connector
     *
     * @param serverConnector new server connector
     *
     * @return old server connector or null if there no old server connector
     */
    public static SocketServer setServerConnector(SocketServer serverConnector) {
        SocketServer oldSocketServer = sSocketServer;
        sSocketServer = serverConnector;
        return oldSocketServer;
    }
}
