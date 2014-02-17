package com.gmail.yaroslavlancelot.spaceinvaders.network;

import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreatedClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.ConnectionEstablishClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGame;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.PreGameStart;
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

/**
 * Used by server to communicate with client. Sending and retrieving messages.
 */
public class GameSocketServer extends SocketServer<SocketConnectionClientConnector> implements MessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    //TODO it should be just PreGameStart not a list
    private List<PreGameStart> mPreGameStartList = new ArrayList<PreGameStart>(2);
    private String mClientIp;
    //TODO it should be just InGame not a list
    private List<InGame> mInGameList = new ArrayList<InGame>(2);
    private static GameSocketServer sGameSocketServer;

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
                synchronized (mPreGameStartList) {
                    for (PreGameStart preGameStart : mPreGameStartList) {
                        preGameStart.clientConnectionEstablished(mClientIp);
                    }
                }
            }
        });

        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_BUILDING_CREATED, BuildingCreatedClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageFromClient(TAG, "connection with client established");
                int buildingId = ((BuildingCreatedClientMessage) pClientMessage).getBuildingId();
                synchronized (mInGameList) {
                    for (InGame inGame : mInGameList) {
                        inGame.newBuildingCreate(buildingId);
                    }
                }
            }
        });

        return clientConnector;
    }

    public String getClientIp() {
        return mClientIp;
    }


    public void addPreGameStartCallbacks(PreGameStart preGameStart) {
        synchronized (mPreGameStartList) {
            mPreGameStartList.add(preGameStart);
        }
    }

    public void removePreGameStartCallbacks(PreGameStart preGameStart) {
        synchronized (mPreGameStartList) {
            mPreGameStartList.add(preGameStart);
        }
    }

    public void addInGameCallbacks(InGame inGame) {
        synchronized (mPreGameStartList) {
            mInGameList.add(inGame);
        }
    }

    public void removeInGameCallbacks(InGame inGame) {
        synchronized (mPreGameStartList) {
            mInGameList.remove(inGame);
        }
    }

    public static GameSocketServer getGameSocketServer() {
        return sGameSocketServer;
    }

    public static GameSocketServer setGameSocketServer(GameSocketServer gameSocketServer) {
        GameSocketServer oldGameSocketServer = sGameSocketServer;
        sGameSocketServer = gameSocketServer;
        return oldGameSocketServer;
    }
}
