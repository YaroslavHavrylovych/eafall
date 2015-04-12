package com.gmail.yaroslavlancelot.eafall.network.client.connector;

import com.gmail.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.BuildingCreatedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.GameStartedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.GameObjectHealthChangedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.MoneyChangedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.StartingGameServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitChangePositionServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitCreatedServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.UnitFireServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.server.messages.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.eafall.network.client.callbacks.InGameClient;
import com.gmail.yaroslavlancelot.eafall.network.client.callbacks.PreGameStartClient;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

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
public class GameServerConnector extends ServerConnector<SocketConnection> implements ServerMessagesConstants {
    public static final String TAG = GameServerConnector.class.getCanonicalName();
    private static volatile GameServerConnector sGameServerConnector;
    private final String mServerIp;
    private final List<PreGameStartClient> mPreGameStartClientList = new ArrayList<PreGameStartClient>(2);
    private final List<InGameClient> mInGameClientList = new ArrayList<InGameClient>(2);

    public GameServerConnector(final String serverIP, int serverPort, final SocketConnectionServerConnector.ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
        super(new SocketConnection(new Socket(serverIP, serverPort)), pSocketConnectionServerConnectorListener);
        mServerIp = serverIP;

        registerServerMessage(WAITING_FOR_PLAYERS, WaitingForPlayersServerMessage.class, new IServerMessageHandler<SocketConnection>() {
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

        registerServerMessage(STARTING_GAME, StartingGameServerMessage.class, new IServerMessageHandler<SocketConnection>() {
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

        registerServerMessage(BUILDING_CREATED, BuildingCreatedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
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

        registerServerMessage(UNIT_CREATED, UnitCreatedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
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

        registerServerMessage(UNIT_MOVED, UnitChangePositionServerMessage.class, new IServerMessageHandler<SocketConnection>() {
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

        registerServerMessage(GAME_OBJECT_HEALTH_CHANGED, GameObjectHealthChangedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "game object health changed");
                GameObjectHealthChangedServerMessage gameObjectHealthChangedServerMessage = (GameObjectHealthChangedServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.gameObjectHealthChanged(gameObjectHealthChangedServerMessage.getGameObjectUniqueId(),
                                gameObjectHealthChangedServerMessage.getObjectHealth());
                    }
                }
            }
        });

        registerServerMessage(UNIT_FIRE, UnitFireServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "fireFromPosition on server");
                UnitFireServerMessage unitFireServerMessage = (UnitFireServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.unitFire(unitFireServerMessage.getUnitUniqueId(),
                                unitFireServerMessage.getAttackedGameObjectUniqueId());
                    }
                }
            }
        });

        registerServerMessage(MONEY_CHANGED, MoneyChangedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "money changed on server");
                MoneyChangedServerMessage unitFireServerMessage = (MoneyChangedServerMessage) pServerMessage;
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.moneyChanged(unitFireServerMessage.getTeamName(), unitFireServerMessage.getMoney());
                    }
                }
            }
        });

        registerServerMessage(GAME_STARTED, GameStartedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageInClient(TAG, "game started");
                synchronized (mInGameClientList) {
                    for (InGameClient inGameClient : mInGameClientList) {
                        inGameClient.gameStarted();
                    }
                }
            }
        });
    }

    public static GameServerConnector getGameServerConnector() {
        return sGameServerConnector;
    }

    public static GameServerConnector setGameServerConnector(GameServerConnector gameServerConnector) {
        GameServerConnector oldGameServerConnector = sGameServerConnector;
        sGameServerConnector = gameServerConnector;
        return oldGameServerConnector;
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
}