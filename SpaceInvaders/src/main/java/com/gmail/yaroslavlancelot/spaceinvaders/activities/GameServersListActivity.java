package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client.ConnectionEstablishClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.client.PreGameStartClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.protocol.exception.WifiException;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.IPUtils;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * activity which contains list about all available games in the network and from this activity you can perform all
 * operations with network in this game. Like invoke server creation activity or connect to server etc.
 */
public class GameServersListActivity extends BaseNonGameActivity implements
        // for discovering servers
        SocketServerDiscoveryClient.ISocketServerDiscoveryClientListener,
        // for collaborate with discovered servers
        SocketConnectionServerConnector.ISocketConnectionServerConnectorListener,
        // to handle callbacks from server
        PreGameStartClient {

    public static final String TAG = GameServersListActivity.class.getCanonicalName();
    /** for discovering servers */
    private SocketServerDiscoveryClient mSocketServerDiscoveryClient;
    /** list about all available servers */
    private ListView mServersListView;
    /** adapter to display available servers (for list view) */
    private ArrayAdapter<String> mArrayAdapter;
    /** all available servers will stored in this map to have access to their connectors by ip */
    private Map<String, GameServerConnector> mServerConnectorMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_game_layout);
        initBackButton(findViewById(R.id.back));
        initGamesList((ListView) findViewById(R.id.games_list_list_view));
        initCreateServerButton(findViewById(R.id.create_game));
        initDirectIpButton(findViewById(R.id.direct_ip));
    }

    private void initBackButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                GameServersListActivity.this.finish();
            }
        });
    }

    private void initGamesList(ListView listView) {
        if (listView == null) return;
        mServersListView = listView;
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.server_item_view, R.id.server_ip_string);
        mServersListView.setAdapter(mArrayAdapter);
    }

    private void initCreateServerButton(View createServerButton) {
        if (createServerButton == null) return;
        createServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(GameServersListActivity.this, ServerGameCreationActivity.class);
                startActivity(singleGameIntent);
            }
        });
    }

    private void initDirectIpButton(View directIpButton) {
        if (directIpButton == null) return;
        directIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Dialog directIpDialog = new Dialog(GameServersListActivity.this);
                directIpDialog.setContentView(R.layout.direct_ip_layout);
                directIpDialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        directIpDialog.dismiss();
                    }
                });

                directIpDialog.findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        EditText ipAddressEditText = (EditText) directIpDialog.findViewById(R.id.ip_address_edit_text);
                        String ipAddress = ipAddressEditText.getText().toString();

                        initClient(ipAddress, SocketDiscoveryServer.SERVER_PORT);

                        directIpDialog.dismiss();
                    }
                });

                directIpDialog.show();
            }
        });
    }

    private void initClient(final String ipAddress, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mServerConnectorMap) {
                        GameServerConnector serverConnector = new GameServerConnector(ipAddress, port, GameServersListActivity.this);
                        serverConnector.addPreGameStartCallback(GameServersListActivity.this);
                        serverConnector.start();
                        mServerConnectorMap.put(ipAddress, serverConnector);
                    }
                } catch (final Throwable t) {
                    LoggerHelper.printErrorMessage(TAG, t.toString());
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSocketDiscoveryClient();
        stopClients();
    }

    private void stopSocketDiscoveryClient() {
        if (mSocketServerDiscoveryClient != null) {
            mSocketServerDiscoveryClient.terminate();
            mSocketServerDiscoveryClient = null;
        }
    }

    private void stopClients() {
        if (mServerConnectorMap != null) {
            synchronized (mServerConnectorMap) {
                for (String serverIp : mServerConnectorMap.keySet()) {
                    mServerConnectorMap.get(serverIp).terminate();
                }
            }
            mServerConnectorMap = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSocketDiscoveryClient();
        stopClients();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initSocketDiscoveryClient();
        } catch (WifiException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mSocketServerDiscoveryClient.discoverAsync();
    }

    private void initSocketDiscoveryClient() throws WifiException, UnknownHostException {
        mServerConnectorMap = new HashMap<String, GameServerConnector>(5);
        //ListView
        mServersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                TextView serverIpTextView = (TextView) view.findViewById(R.id.server_ip_string);
                GameServerConnector gameServerConnector;
                synchronized (mServerConnectorMap) {
                    gameServerConnector = mServerConnectorMap.remove(serverIpTextView.getText());
                }
                if (gameServerConnector == null) return;
                try {
                    gameServerConnector.sendClientMessage(new ConnectionEstablishClientMessage(ConnectionEstablishClientMessage.PROTOCOL_VERSION));
                } catch (IOException e) {
                    LoggerHelper.printErrorMessage(TAG, e.toString());
                    return;
                }
                gameServerConnector.removePreGameStartCallback(GameServersListActivity.this);
                GameServerConnector.setGameServerConnector(gameServerConnector);
                // start new activity
                Intent connectedToServerActivityIntent = new Intent(GameServersListActivity.this, ClientWaitForGameActivity.class);
                startActivity(connectedToServerActivityIntent);
            }
        });
        //create SocketServerDiscoveryClient
        mSocketServerDiscoveryClient = new SocketServerDiscoveryClient(WifiUtils.getBroadcastIPAddressRaw(this),
                IDiscoveryData.DefaultDiscoveryData.class, this);
    }

    @Override
    public void onStarted(final ServerConnector<SocketConnection> serverConnector) {
        LoggerHelper.printInformationMessage(TAG, "CLIENT: Connected to server.");
    }

    @Override
    public void onTerminated(final ServerConnector<SocketConnection> serverConnector) {
        LoggerHelper.printInformationMessage(TAG, "Disconnected from Server.");
    }

    @Override
    public void gameStart(final String serverIP) {
        gameStop(serverIP);
    }

    @Override
    public void gameStop(final String serverIP) {
        synchronized (mServersListView) {
            mArrayAdapter.remove(serverIP);
        }
    }

    @Override
    public void gameWaitingForPlayers(final String serverIP) {
        mServersListView.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mServersListView) {
                    mArrayAdapter.add(serverIP);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDiscovery(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final IDiscoveryData pDiscoveryData) {
        IDiscoveryData.DefaultDiscoveryData discoveryData = (IDiscoveryData.DefaultDiscoveryData) pDiscoveryData;
        try {
            final String ipAddressAsString = IPUtils.ipAddressToString(discoveryData.getServerIP());
            LoggerHelper.printInformationMessage(TAG, "DiscoveryClient: Server discovered at: " + ipAddressAsString + ":" + discoveryData.getServerPort());
            initClient(ipAddressAsString, discoveryData.getServerPort());
        } catch (final UnknownHostException e) {
            LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: IPException: " + e);
        }
    }

    @Override
    public void onTimeout(final SocketServerDiscoveryClient socketServerDiscoveryClient, final SocketTimeoutException socketTimeoutException) {
        LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: Timeout: " + socketTimeoutException);
    }

    @Override
    public void onException(final SocketServerDiscoveryClient socketServerDiscoveryClient, final Throwable throwable) {
        LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: Exception: " + throwable);
    }
}
