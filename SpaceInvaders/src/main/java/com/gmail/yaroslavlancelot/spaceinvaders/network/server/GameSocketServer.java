package com.gmail.yaroslavlancelot.spaceinvaders.network.server;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.buildings.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.ConnectionEstablishedClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.GameLoadedClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.constants.ClientMessagesConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.network.server.callbacks.InGameServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.server.callbacks.PreGameStartServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Used by server to communicate with client. Sending and retrieving messages.
 */
public class GameSocketServer extends SocketServer<SocketConnectionClientConnector> implements ClientMessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    private static GameSocketServer sGameSocketServer;
    private PreGameStartServer mPreGameStartServer;
    private String mClientIp;
    private InGameServer mInGameServer;

    public GameSocketServer(final int pPort, final ClientConnector.IClientConnectorListener<SocketConnection> pClientConnectorListener) {
        super(pPort, pClientConnectorListener);
    }

    public static GameSocketServer getGameSocketServer() {
        return sGameSocketServer;
    }

    public static GameSocketServer setGameSocketServer(GameSocketServer gameSocketServer) {
        GameSocketServer oldGameSocketServer = sGameSocketServer;
        sGameSocketServer = gameSocketServer;
        return oldGameSocketServer;
    }

    @Override
    protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
        mClientIp = pSocketConnection.getSocket().getInetAddress().getHostAddress();
        LoggerHelper.printInformationMessage(TAG, "New client connector created with client " + mClientIp);
        final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);

        clientConnector.registerClientMessage(CONNECTION_ESTABLISHED, ConnectionEstablishedClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "connection with client established");
                int protocolVersion = ((ConnectionEstablishedClientMessage) pClientMessage).getProtocolVersion();
                LoggerHelper.printDebugMessage(TAG, "protocolVersion=" + protocolVersion);
                synchronized (mPreGameStartServer) {
                    mPreGameStartServer.clientConnectionEstablished(mClientIp);
                }
            }
        });

        clientConnector.registerClientMessage(BUILDING_CREATION, BuildingCreationClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "client want to create a building");
                BuildingCreationClientMessage message = (BuildingCreationClientMessage) pClientMessage;
                //ToDo Remove EventBus
                EventBus.getDefault().post(new CreateBuildingEvent(message.getTeamName(), message.getBuildingId()));
            }
        });


        clientConnector.registerClientMessage(GAME_LOADED, GameLoadedClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "client loaded game");
                synchronized (mInGameServer) {
                    mInGameServer.gameLoaded();
                }
            }
        });

        return clientConnector;
    }

    public String getClientIp() {
        return mClientIp;
    }

    public void addPreGameStartCallback(PreGameStartServer preGameStartServer) {
        this.mPreGameStartServer = preGameStartServer;
    }

    public void removePreGameStartCallback() {
        this.mPreGameStartServer = null;
    }

    public void addInGameCallback(InGameServer inGameServer) {
        mInGameServer = inGameServer;
    }

    public void removeInGameCallback() {
        mInGameServer = null;
    }
}
