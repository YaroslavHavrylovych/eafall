package com.gmail.yaroslavlancelot.spaceinvaders.network;

import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.ConnectionEstablishClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.PreGameStartCallbacksFromClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameSocketServer extends SocketServer<SocketConnectionClientConnector> implements MessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    private List<PreGameStartCallbacksFromClient> mPreGameStartCallbacksFromClientList = new ArrayList<PreGameStartCallbacksFromClient>(2);
    private String mClientIp;

    public GameSocketServer(final int pPort, final ClientConnector.IClientConnectorListener<SocketConnection> pClientConnectorListener) {
        super(pPort, pClientConnectorListener);
    }

    @Override
    protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
        mClientIp = pSocketConnection.getSocket().getInetAddress().getHostAddress();
        LoggerHelper.printInformationMessage(TAG, "New client connector created with client " + mClientIp);
        final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);

        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISHED, ConnectionEstablishClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageFromClient(TAG, "connection with client established");
                synchronized (mPreGameStartCallbacksFromClientList) {
                    for(PreGameStartCallbacksFromClient preGameStartCallbacksFromClient : mPreGameStartCallbacksFromClientList) {
                        preGameStartCallbacksFromClient.clientConnectionEstablished(mClientIp);
                    }
                }
            }
        });

        return clientConnector;
    }

    public String getClientIp() {
        return mClientIp;
    }


    public void addPreGameStartCallbacks(PreGameStartCallbacksFromClient preGameStartCallbacksFromClient) {
        synchronized (mPreGameStartCallbacksFromClientList) {
            mPreGameStartCallbacksFromClientList.add(preGameStartCallbacksFromClient);
        }
    }

    public void removePreGameStartCallbacks(PreGameStartCallbacksFromClient preGameStartCallbacksFromClient) {
        synchronized (mPreGameStartCallbacksFromClientList) {
            mPreGameStartCallbacksFromClientList.add(preGameStartCallbacksFromClient);
        }
    }
}
