package com.gmail.yaroslavlancelot.eafall.network.server;

import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.network.client.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.BuildingCreationClientMessage;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.ConnectionEstablishedClientMessage;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.GameLoadedClientMessage;
import com.gmail.yaroslavlancelot.eafall.network.client.messages.constants.ClientMessagesConstants;
import com.gmail.yaroslavlancelot.eafall.network.server.callbacks.InGameServer;
import com.gmail.yaroslavlancelot.eafall.network.server.callbacks.PreGameStartServer;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.extension.multiplayer.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.server.SocketServer;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.shared.SocketConnection;

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
                LoggerHelper.printInformationMessage(TAG, "RECEIVED MESSAGE: sender = CLIENT, type = " + CONNECTION_ESTABLISHED);
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
                LoggerHelper.printInformationMessage(TAG, "RECEIVED MESSAGE: sender = CLIENT, type = " + BUILDING_CREATION);
                BuildingCreationClientMessage message = (BuildingCreationClientMessage) pClientMessage;
                //ToDo Remove EventBus
                EventBus.getDefault().post(new CreateBuildingEvent(message.getPlayerName(), message.getBuildingId()));
            }
        });


        clientConnector.registerClientMessage(GAME_LOADED, GameLoadedClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                LoggerHelper.printInformationMessage(TAG, "RECEIVED MESSAGE: sender = CLIENT, type = " + GAME_LOADED);
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