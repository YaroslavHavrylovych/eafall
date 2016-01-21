package com.yaroslavlancelot.eafall.android.activities.multiplayer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.yaroslavlancelot.eafall.network.client.callbacks.PreGameStartClient;
import com.yaroslavlancelot.eafall.network.client.connector.GameServerConnector;
import com.yaroslavlancelot.eafall.network.client.messages.ConnectionEstablishedClientMessage;
import com.yaroslavlancelot.eafall.network.server.discovery.SocketDiscoveryServer;

import org.andengine.extension.multiplayer.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.util.IPUtils;
import org.andengine.util.WifiUtils;

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
    protected void onPause() {
        super.onPause();
        stopSocketDiscoveryClient();
        stopClients();
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
        } catch (WifiUtils.WifiUtilsException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mSocketServerDiscoveryClient.discoverAsync();
    }

    @Override
    protected void onCustomCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.network_game_layout);
        initBackButton(findViewById(R.id.back));
        initGamesList((ListView) findViewById(R.id.games_list_list_view));
        initCreateServerButton(findViewById(R.id.create_game));
        initDirectIpButton(findViewById(R.id.direct_ip));
    }

    @Override
    public void onStarted(final ServerConnector<SocketConnection> serverConnector) {
        //TODO logger was here
    }

    @Override
    public void onTerminated(final ServerConnector<SocketConnection> serverConnector) {
        //TODO logger was here
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
            //TODO logger was here
            initClient(ipAddressAsString, discoveryData.getServerPort());
        } catch (final UnknownHostException e) {
            //TODO logger was here
        }
    }

    @Override
    public void onTimeout(final SocketServerDiscoveryClient socketServerDiscoveryClient, final SocketTimeoutException socketTimeoutException) {
        //TODO logger was here
    }

    @Override
    public void onException(final SocketServerDiscoveryClient socketServerDiscoveryClient, final Throwable throwable) {
        //TODO logger was here
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
                    //TODO logger was here
                }
            }
        }).start();
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

    private void initSocketDiscoveryClient() throws UnknownHostException, WifiUtils.WifiUtilsException {
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
                gameServerConnector.sendClientMessage(0,
                        new ConnectionEstablishedClientMessage(ConnectionEstablishedClientMessage.PROTOCOL_VERSION));
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
}
