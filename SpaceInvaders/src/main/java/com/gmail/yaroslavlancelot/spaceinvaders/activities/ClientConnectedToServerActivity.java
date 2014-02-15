package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;

public class ClientConnectedToServerActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_connected_to_server_layout);
        initServerIpView(findViewById(R.id.server_ip_string), GameServerConnector.getGameServerConnector().getServerIp());
    }

    private void initServerIpView(View view, String serverIpString) {
        if (view == null) return;
        TextView serverIpTextView = (TextView) view;
        serverIpTextView.setText(serverIpString);
    }
}
