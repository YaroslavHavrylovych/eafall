package com.gmail.yaroslavlancelot.eafall.network.server.connector;

import com.gmail.yaroslavlancelot.eafall.android.activities.multiplayer.ServerGameCreationActivity;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.shared.SocketConnection;

import java.io.IOException;

public class ClientConnectorListener implements SocketConnectionClientConnector.ISocketConnectionClientConnectorListener {
    @Override
    public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
        LoggerHelper.printInformationMessage(ServerGameCreationActivity.TAG, "SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        //TODO check priority of all such messages
        pClientConnector.sendServerMessage(0, new WaitingForPlayersServerMessage());
    }
    @Override
    public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
        LoggerHelper.printInformationMessage(ServerGameCreationActivity.TAG, "SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());

    }
}
