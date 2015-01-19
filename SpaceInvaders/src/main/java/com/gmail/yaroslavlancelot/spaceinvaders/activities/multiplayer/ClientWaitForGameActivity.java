package com.gmail.yaroslavlancelot.spaceinvaders.activities.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.BaseNonGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.ClientGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.rebels.Rebels;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.callbacks.PreGameStartClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.connector.GameServerConnector;

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
        Intent clientGameIntent = new Intent(ClientWaitForGameActivity.this, ClientGameActivity.class);
        clientGameIntent.
                putExtra(StringsAndPathUtils.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, TeamControlBehaviourType.USER_CONTROL_ON_CLIENT_SIDE.toString()).
                putExtra(StringsAndPathUtils.SECOND_TEAM_ALLIANCE, Rebels.ALLIANCE_NAME).
                putExtra(StringsAndPathUtils.FIRST_TEAM_ALLIANCE, Imperials.ALLIANCE_NAME).
                putExtra(StringsAndPathUtils.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE.toString());
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
