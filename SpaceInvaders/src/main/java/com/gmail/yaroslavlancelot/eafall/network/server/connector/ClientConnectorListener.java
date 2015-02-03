package com.gmail.yaroslavlancelot.eafall.network.server.connector;

import com.gmail.yaroslavlancelot.eafall.android.activities.multiplayer.ServerGameCreationActivity;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import java.io.IOException;

public class ClientConnectorListener implements SocketConnectionClientConnector.ISocketConnectionClientConnectorListener {
    @Override
    public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
        LoggerHelper.printInformationMessage(ServerGameCreationActivity.TAG, "SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        try {
            pClientConnector.sendServerMessage(new WaitingForPlayersServerMessage());
        } catch (IOException e) {
            LoggerHelper.printErrorMessage(ServerGameCreationActivity.TAG, "Error while sending message to client: " + e.getMessage());
        }
    }
    @Override
    public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
        LoggerHelper.printInformationMessage(ServerGameCreationActivity.TAG, "SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());

    }
}
