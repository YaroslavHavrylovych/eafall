package com.yaroslavlancelot.eafall.network.server.connector;

import com.yaroslavlancelot.eafall.android.activities.multiplayer.ServerGameCreationActivity;
import com.yaroslavlancelot.eafall.network.server.messages.WaitingForPlayersServerMessage;

import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.shared.SocketConnection;

public class ClientConnectorListener implements SocketConnectionClientConnector.ISocketConnectionClientConnectorListener {
    @Override
    public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
        //TODO logger was here
        //TODO check priority of all such messages
        pClientConnector.sendServerMessage(0, new WaitingForPlayersServerMessage());
    }
    @Override
    public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
        //TODO logger was here

    }
}
