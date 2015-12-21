package com.yaroslavlancelot.eafall.android.activities.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.BaseNonGameActivity;
import com.yaroslavlancelot.eafall.game.client.thin.ThinClientGameActivity;
import com.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.network.client.callbacks.PreGameStartClient;
import com.yaroslavlancelot.eafall.network.client.connector.GameServerConnector;

/**
 * Client connected to server and waiting for game
 */
public class ClientWaitForGameActivity extends BaseNonGameActivity implements PreGameStartClient {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_connected_to_server_layout);
        GameServerConnector gameServerConnector = GameServerConnector.getGameServerConnector();
        if (gameServerConnector == null) return;
        String serverIp = gameServerConnector.getServerIp();
        initServerIpTextView(findViewById(R.id.server_ip_string), serverIp);
        GameServerConnector.getGameServerConnector().addPreGameStartCallback(this);
    }

    /**
     * display server ip address (so client can know where he is connected)
     *
     * @param view           to display server ip address
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
        Intent clientGameIntent = new Intent(ClientWaitForGameActivity.this, ThinClientGameActivity.class);
        clientGameIntent.
                putExtra(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE, IPlayer.ControlType.USER_CONTROL_ON_CLIENT_SIDE.toString()).
                putExtra(StringConstants.SECOND_PLAYER_ALLIANCE, Rebels.ALLIANCE_NAME).
                putExtra(StringConstants.FIRST_PLAYER_ALLIANCE, Imperials.ALLIANCE_NAME).
                putExtra(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE, IPlayer.ControlType.REMOTE_CONTROL_ON_CLIENT_SIDE.toString());
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