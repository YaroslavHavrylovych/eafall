package com.gmail.yaroslavlancelot.spaceinvaders.network.connector;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.StartingGameServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.UnitCreatedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.InGameClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.PreGameStartClient;
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

/**
 * Used by client to operate with server. Sending and retrieving messages.
 */
public class GameServerConnector extends ServerConnector<SocketConnection> implements MessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    private final String mServerIp;
    private List<PreGameStartClient> mPreGameStartClientList = new ArrayList<PreGameStartClient>(2);
    private List<InGameClient> mInGameClientList = new ArrayList<InGameClient>(2);
    private static volatile GameServerConnector sGameServerConnector;

    public GameServerConnector(final String serverIP, int serverPort, final SocketConnectionServerConnector.ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
        super(new SocketConnection(new Socket(serverIP, serverPort)), pSocketConnectionServerConnectorListener);
        mServerIp = serverIP;

        registerServerMessage(FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS, WaitingForPlayersServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "server waiting for players");
                synchronized (mPreGameStartClientList) {
                    for (PreGameStartClient preGameStartClient : mPreGameStartClientList) {
                        preGameStartClient.gameWaitingForPlayers(mServerIp);
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_STARTING_GAME, StartingGameServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "starting game");
                synchronized (mPreGameStartClientList) {
                    for (PreGameStartClient preGameStartClient : mPreGameStartClientList) {
                        preGameStartClient.gameStart(mServerIp);
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_BUILDING_CREATED, BuildingCreatedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "building created");
                BuildingCreatedServerMessage buildingCreatedServerMessage = (BuildingCreatedServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.buildingCreated(buildingCreatedServerMessage.getBuildingId(), buildingCreatedServerMessage.getTeamName());
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_UNIT_CREATED, UnitCreatedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "unit created");
                UnitCreatedServerMessage unitCreatedServerMessage = (UnitCreatedServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.unitCreated(unitCreatedServerMessage.getTeamName(), unitCreatedServerMessage.getUnitId(),
                                unitCreatedServerMessage.getX(), unitCreatedServerMessage.getY(), unitCreatedServerMessage.getUnitUniqueId());
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_UNIT_MOVED, UnitChangePositionServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "unit moved");
                UnitChangePositionServerMessage unitChangePositionServerMessage = (UnitChangePositionServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.unitMoved(unitChangePositionServerMessage);
                    }
                }
            }
        });
    }

    public String getServerIp() {
        return mServerIp;
    }

    public void addPreGameStartCallback(PreGameStartClient preGameStartClient) {
        synchronized (mPreGameStartClientList) {
            mPreGameStartClientList.add(preGameStartClient);
        }
    }

    public void removePreGameStartCallback(PreGameStartClient preGameStartClient) {
        synchronized (mPreGameStartClientList) {
            mPreGameStartClientList.remove(preGameStartClient);
        }
    }

    public void addInGameCallback(InGameClient inGameClient) {
        synchronized (mInGameClientList) {
            mInGameClientList.add(inGameClient);
        }
    }

    public void removeInGameCallback(InGameClient inGameClient) {
        synchronized (mInGameClientList) {
            mInGameClientList.remove(inGameClient);
        }
    }


    public static GameServerConnector getGameServerConnector() {
        return sGameServerConnector;
    }

    public static GameServerConnector setGameServerConnector(GameServerConnector gameServerConnector) {
        GameServerConnector oldGameServerConnector = sGameServerConnector;
        sGameServerConnector = gameServerConnector;
        return oldGameServerConnector;
    }
}
