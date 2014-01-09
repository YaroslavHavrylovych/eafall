package com.gmail.yaroslavlancelot.spaceinvaders.network;

import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import java.io.IOException;

public class GameSocketServer extends SocketServer<SocketConnectionClientConnector> {
    public GameSocketServer(final int pPort, final ClientConnector.IClientConnectorListener<SocketConnection> pClientConnectorListener) {
        super(pPort, pClientConnectorListener);
    }

    @Override
    protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
        final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);

        return clientConnector;
    }
}
