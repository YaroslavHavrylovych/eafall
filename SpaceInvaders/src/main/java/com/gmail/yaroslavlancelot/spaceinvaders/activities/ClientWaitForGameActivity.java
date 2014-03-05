package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.ClientGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.PreGameStart;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.GameServerConnector;

/**
 * Client connected to server and waiting for game
 */
public class ClientWaitForGameActivity extends Activity implements PreGameStart {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_connected_to_server_layout);
        initServerIpTextView(findViewById(R.id.server_ip_string), GameServerConnector.getGameServerConnector().getServerIp());
        GameServerConnector.getGameServerConnector().addPreGameStartCallback(this);
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

    @Override
    public void gameStart(final String serverIP) {
        GameServerConnector.getGameServerConnector().removePreGameStartCallback(this);
        Intent clientGameIntent = new Intent(ClientWaitForGameActivity.this, ClientGameActivity.class);
        clientGameIntent.
                putExtra(GameStringsConstantsAndUtils.RED_TEAM_NAME, TeamControlBehaviourType.USER_CONTROL.toString()).
                putExtra(GameStringsConstantsAndUtils.BLUE_TEAM_NAME, TeamControlBehaviourType.REMOTE_CONTROL.toString());
        startActivity(clientGameIntent);
    }

    @Override
    public void gameStop(final String serverIP) {
        throw new UnsupportedOperationException("server send stop to unstarted game");
    }

    @Override
    public void gameWaitingForPlayers(final String serverIP) {
        throw new UnsupportedOperationException("game in state WaitingForPlayers trying to set this state again");
    }
}
