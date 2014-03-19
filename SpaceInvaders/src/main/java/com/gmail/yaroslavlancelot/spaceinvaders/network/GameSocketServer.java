package com.gmail.yaroslavlancelot.spaceinvaders.network;

import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.ConnectionEstablishClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.PreGameStartServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.InGameServer;
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
    //TODO it should be just PreGameStartServer not a list
    private List<PreGameStartServer> mPreGameStartServerList = new ArrayList<PreGameStartServer>(2);
    private String mClientIp;
    //TODO it should be just InGameServer not a list
    private List<InGameServer> mInGameServerList = new ArrayList<InGameServer>(2);
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
                LoggerHelper.printInformationMessageInClient(TAG, "connection with client established");
                int protocolVersion = ((ConnectionEstablishClientMessage) pClientMessage).getProtocolVersion();
                LoggerHelper.printDebugMessage(TAG, "protocolVersion=" + protocolVersion);
                synchronized (mPreGameStartServerList) {
                    for (PreGameStartServer preGameStartServer : mPreGameStartServerList) {
                        preGameStartServer.clientConnectionEstablished(mClientIp);
                    }
                }
            }
        });

        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING, BuildingCreationClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "client want to create a building");
                int buildingId = ((BuildingCreationClientMessage) pClientMessage).getBuildingId();
                synchronized (mInGameServerList) {
                    for (InGameServer inGameServer : mInGameServerList) {
                        inGameServer.newBuildingCreate(buildingId);
                    }
                }
            }
        });

        return clientConnector;
    }

    public String getClientIp() {
        return mClientIp;
    }


    public void addPreGameStartCallback(PreGameStartServer preGameStartServer) {
        synchronized (mPreGameStartServerList) {
            mPreGameStartServerList.add(preGameStartServer);
        }
    }

    public void removePreGameStartCallback(PreGameStartServer preGameStartServer) {
        synchronized (mPreGameStartServerList) {
            mPreGameStartServerList.add(preGameStartServer);
        }
    }

    public void addInGameCallbacks(InGameServer inGameServer) {
        synchronized (mPreGameStartServerList) {
            mInGameServerList.add(inGameServer);
        }
    }

    public void removeInGameCallbacks(InGameServer inGameServer) {
        synchronized (mPreGameStartServerList) {
            mInGameServerList.remove(inGameServer);
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
