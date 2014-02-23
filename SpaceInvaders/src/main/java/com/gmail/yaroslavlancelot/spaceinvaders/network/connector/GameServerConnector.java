package com.gmail.yaroslavlancelot.spaceinvaders.network.connector;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.StartingGameServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.PreGameStart;
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
    private List<PreGameStart> mPreGameStartList = new ArrayList<PreGameStart>(2);
    private static volatile GameServerConnector sGameServerConnector;

    public GameServerConnector(final String serverIP, int serverPort, final SocketConnectionServerConnector.ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
        super(new SocketConnection(new Socket(serverIP, serverPort)), pSocketConnectionServerConnectorListener);
        mServerIp = serverIP;

        registerServerMessage(FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS, WaitingForPlayersServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageFromClient(TAG, "server waiting for players");
                synchronized (mPreGameStartList) {
                    for (PreGameStart preGameStart : mPreGameStartList) {
                        preGameStart.gameWaitingForPlayers(mServerIp);
                    }
                }
            }
        });

        registerServerMessage(FLAG_MESSAGE_SERVER_STARTING_GAME, StartingGameServerMessage.class, new IServerMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                LoggerHelper.printInformationMessageFromClient(TAG, "starting game");
                synchronized (mPreGameStartList) {
                    for (PreGameStart preGameStart : mPreGameStartList) {
                        preGameStart.gameStart(mServerIp);
                    }
                }
            }
        });
    }

    public String getServerIp() {
        return mServerIp;
    }

    public void addPreGameStartCallback(PreGameStart preGameStart) {
        synchronized (mPreGameStartList) {
            mPreGameStartList.add(preGameStart);
        }
    }

    public void removePreGameStartCallback(PreGameStart preGameStart) {
        synchronized (mPreGameStartList) {
            mPreGameStartList.remove(preGameStart);
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
