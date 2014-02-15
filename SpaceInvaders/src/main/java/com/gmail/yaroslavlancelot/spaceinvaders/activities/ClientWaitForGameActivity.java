package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;

/**
 * Client connected to server and waiting for game
 */
public class ClientWaitForGameActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_connected_to_server_layout);
        initServerIpTextView(findViewById(R.id.server_ip_string), GameServerConnector.getGameServerConnector().getServerIp());
    }

    /**
     * display server ip address (so client can know where he is connected)
     *
     * @param view to display server ip address
     * @param serverIpString server ip to be shown
     */
    private void initServerIpTextView(View view, String serverIpString) {
        if (view == null) return;
        TextView serverIpTextView = (TextView) view;
        serverIpTextView.setText(serverIpString);
    }
}
