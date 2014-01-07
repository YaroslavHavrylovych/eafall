package com.gmail.yaroslavlancelot.spaceinvaders.network;

import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.PreGameStartCallback;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionCloseServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionEstablishedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServerConnector extends ServerConnector<SocketConnection> implements MessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    private final String mServerIp;
    private List<PreGameStartCallback> mPreGameStartCallbackList = new ArrayList<PreGameStartCallback>(2);

    public GameServerConnector(final String serverIP, int serverPort, final SocketConnectionServerConnector.ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
        super(new SocketConnection(new Socket(serverIP, serverPort)), pSocketConnectionServerConnectorListener);
        mServerIp = serverIP;

        registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessage(TAG, "CLIENT: Connection closed.");
                synchronized (mPreGameStartCallbackList) {
                    for (PreGameStartCallback preGameStartCallback : mPreGameStartCallbackList) {
                        preGameStartCallback.gameStop(mServerIp);
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_GAME_START, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                synchronized (mPreGameStartCallbackList) {
                    for (PreGameStartCallback preGameStartCallback : mPreGameStartCallbackList) {
                        preGameStartCallback.gameStart(mServerIp);
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessage(TAG, "CLIENT: Connection established.");
                synchronized (mPreGameStartCallbackList) {
                    for (PreGameStartCallback preGameStartCallback : mPreGameStartCallbackList) {
                        preGameStartCallback.gameWaitingForPlayers(mServerIp);
                    }
                }
            }
        });
    }

    public void addPreGameStartCallbacks(PreGameStartCallback preGameStartCallback) {
        synchronized (mPreGameStartCallbackList) {
            mPreGameStartCallbackList.add(preGameStartCallback);
        }
    }
}
