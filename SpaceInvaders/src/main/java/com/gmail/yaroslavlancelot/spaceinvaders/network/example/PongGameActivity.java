package com.gmail.yaroslavlancelot.spaceinvaders.network.example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.client.ConnectionPingClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.client.MovePaddleClientMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionCloseServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionEstablishedServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionPongServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.ConnectionRejectedProtocolMissmatchServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.SetPaddleIDServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.UpdateBallServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.UpdatePaddleServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.adt.messages.server.UpdateScoreServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.common.ICommonMessages;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.constants.PongConstants;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 19:36:45 - 28.02.2011
 */
public class PongGameActivity extends BaseGameActivity implements PongConstants, IOnSceneTouchListener, ICommonMessages {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final int MENU_PING = Menu.FIRST;
    private static final int CAMERA_WIDTH = GAME_WIDTH;
    private static final int CAMERA_HEIGHT = GAME_HEIGHT;
    private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = MENU_PING + 1;
    private static final int DIALOG_ENTER_SERVER_IP_ID = MENU_PING + DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
    private static final int DIALOG_SHOW_SERVER_IP_ID = MENU_PING + DIALOG_ENTER_SERVER_IP_ID + 1;
    private static final int PADDLEID_NOT_SET = -1;
    // ===========================================================
    // Fields
    // ===========================================================
    private final SparseArray<Rectangle> mPaddleMap = new SparseArray<Rectangle>();
    private final SparseArray<Text> mScoreChangeableTextMap = new SparseArray<Text>();
    private Camera mCamera;
    private String mServerIP = LOCALHOST_IP;
    private int mPaddleID = PADDLEID_NOT_SET;
    private PongServer mServer;
    private PongServerConnector mServerConnector;
    private Rectangle mBall;
    private BitmapTextureAtlas mScoreFontTexture;
    private Font mScoreFont;
    private float mPaddleCenterY;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        this.mCamera.setCenter(0, 0);

        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
        return engineOptions;
    }

    @Override
    public void onCreateResources(final OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        this.mScoreFontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        mScoreFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, org.andengine.util.color.Color.WHITE.hashCode());
        mScoreFont.load();

        this.mEngine.getTextureManager().loadTexture(this.mScoreFontTexture);
        this.getFontManager().loadFont(this.mScoreFont);

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();

		/* Ball */
        this.mBall = new Rectangle(0, 0, BALL_WIDTH, BALL_HEIGHT, getVertexBufferObjectManager());
        scene.attachChild(this.mBall);

		/* Walls */
        scene.attachChild(new Line(-GAME_WIDTH_HALF + 1, -GAME_HEIGHT_HALF, -GAME_WIDTH_HALF + 1, GAME_HEIGHT_HALF, getVertexBufferObjectManager())); // Left
        scene.attachChild(new Line(GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, getVertexBufferObjectManager())); // Right
        scene.attachChild(new Line(-GAME_WIDTH_HALF, -GAME_HEIGHT_HALF + 1, GAME_WIDTH_HALF, -GAME_HEIGHT_HALF + 1, getVertexBufferObjectManager())); // Top
        scene.attachChild(new Line(-GAME_WIDTH_HALF, GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, getVertexBufferObjectManager())); // Bottom

        scene.attachChild(new Line(0, -GAME_HEIGHT_HALF, 0, GAME_HEIGHT_HALF, getVertexBufferObjectManager())); // Middle

		/* Paddles */
        final Rectangle paddleLeft = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT, getVertexBufferObjectManager());
        final Rectangle paddleRight = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT, getVertexBufferObjectManager());
        this.mPaddleMap.put(PADDLE_LEFT.getOwnerID(), paddleLeft);
        this.mPaddleMap.put(PADDLE_RIGHT.getOwnerID(), paddleRight);

        scene.attachChild(paddleLeft);
        scene.attachChild(paddleRight);

		/* Scores */
        final Text scoreLeft = new Text(0, -GAME_HEIGHT_HALF + SCORE_PADDING, this.mScoreFont, "0", 2, getVertexBufferObjectManager());
        scoreLeft.setPosition(-scoreLeft.getWidth() - SCORE_PADDING, scoreLeft.getY());
        final Text scoreRight = new Text(SCORE_PADDING, -GAME_HEIGHT_HALF + SCORE_PADDING, this.mScoreFont, "0", 2, getVertexBufferObjectManager());
        this.mScoreChangeableTextMap.put(PADDLE_LEFT.getOwnerID(), scoreLeft);
        this.mScoreChangeableTextMap.put(PADDLE_RIGHT.getOwnerID(), scoreRight);

        scene.attachChild(scoreLeft);
        scene.attachChild(scoreRight);

        scene.setOnSceneTouchListener(this);

        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                if (PongGameActivity.this.mPaddleID != PADDLEID_NOT_SET) {
                    try {
                        PongGameActivity.this.mServerConnector.sendClientMessage(new MovePaddleClientMessage(PongGameActivity.this.mPaddleID, PongGameActivity.this.mPaddleCenterY));
                    } catch (final IOException e) {
                        Debug.e(e);
                    }
                }
            }

            @Override
            public void reset() {
            }
        });

        pOnCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        this.mPaddleCenterY = pSceneTouchEvent.getY();
        return true;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onDestroy() {
        if (this.mServer != null) {
            try {
                this.mServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
            } catch (final IOException e) {
                Debug.e(e);
            }
            this.mServer.terminate();
        }

        if (this.mServerConnector != null) {
            this.mServerConnector.terminate();
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(final int pKeyCode, final KeyEvent pEvent) {
        switch (pKeyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
        }
        return super.onKeyUp(pKeyCode, pEvent);
    }

    @Override
    public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
        switch (pItem.getItemId()) {
            case DIALOG_SHOW_SERVER_IP_ID:
                try {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Your Server-IP ...")
                            .setCancelable(false)
                            .setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                } catch (final UnknownHostException e) {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Your Server-IP ...")
                            .setCancelable(false)
                            .setMessage("Error retrieving IP of your Server: " + e)
                            .setPositiveButton(android.R.string.ok, new OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface pDialog, final int pWhich) {
                                    PongGameActivity.this.finish();
                                }
                            })
                            .create().show();
                }
                break;
            case DIALOG_ENTER_SERVER_IP_ID:
                final EditText ipEditText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Enter Server-IP ...")
                        .setCancelable(false)
                        .setView(ipEditText)
                        .setPositiveButton("Connect", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface pDialog, final int pWhich) {
                                PongGameActivity.this.mServerIP = ipEditText.getText().toString();
                                PongGameActivity.this.initClient();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface pDialog, final int pWhich) {
                                PongGameActivity.this.finish();
                            }
                        })
                        .create().show();
                break;
            case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Be Server or Client ...")
                        .setCancelable(false)
                        .setPositiveButton("Client", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface pDialog, final int pWhich) {
                                PongGameActivity.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
                            }
                        })
                        .setNeutralButton("Server", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface pDialog, final int pWhich) {
                                PongGameActivity.this.initServerAndClient();
                                PongGameActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                            }
                        })
                        .create().show();
                break;
            case MENU_PING:
                try {
                    final ConnectionPingClientMessage connectionPingClientMessage = new ConnectionPingClientMessage(); // TODO Pooling
                    connectionPingClientMessage.setTimestamp(System.currentTimeMillis());
                    if (this.mServerConnector != null)
                        this.mServerConnector.sendClientMessage(connectionPingClientMessage);
                } catch (final IOException e) {
                    Debug.e(e);
                }
            default:
                return false;
        }
        return true;
    }

    private void initServerAndClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PongGameActivity.this.initServer();
            }
        }).start();

		/* Wait some time after the server has been started, so it actually can start up. */
        try {
            Thread.sleep(500);
        } catch (final Throwable t) {
            Debug.e(t);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                PongGameActivity.this.initClient();
            }
        }).start();
    }

    private void initServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mServer = new PongServer(new ExampleClientConnectorListener());

                mServer.start();

                mEngine.registerUpdateHandler(mServer);
            }
        }).start();
    }


    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        pMenu.add(0, MENU_PING, 0, "MENU_PING");
        pMenu.add(0, DIALOG_CHOOSE_SERVER_OR_CLIENT_ID, 1, "DIALOG_CHOOSE_SERVER_OR_CLIENT_ID");
        pMenu.add(0, DIALOG_ENTER_SERVER_IP_ID, 2, "DIALOG_ENTER_SERVER_IP_ID");
        pMenu.add(0, DIALOG_SHOW_SERVER_IP_ID, 3, "DIALOG_SHOW_SERVER_IP_ID");
        return true;
    }

    private void initClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerConnector = new PongServerConnector(mServerIP, new ExampleServerConnectorListener());
                    mServerConnector.getConnection().start();
                } catch (final Throwable t) {
                    Debug.e(t);
                }
            }
        }).start();
    }

    public void updateScore(final int pPaddleID, final int pPoints) {
        final Text scoreChangeableText = this.mScoreChangeableTextMap.get(pPaddleID);
        scoreChangeableText.setText(String.valueOf(pPoints));

		/* Adjust position of left Score, so that it doesn't overlap the middle line. */
        if (pPaddleID == PADDLE_LEFT.getOwnerID()) {
            scoreChangeableText.setPosition(-scoreChangeableText.getWidth() - SCORE_PADDING, scoreChangeableText.getY());
        }
    }

    public void setPaddleID(final int pPaddleID) {
        this.mPaddleID = pPaddleID;
    }

    public void updatePaddle(final int pPaddleID, final float pX, final float pY) {
        this.mPaddleMap.get(pPaddleID).setPosition(pX, pY);
    }

    public void updateBall(final float pX, final float pY) {
        this.mBall.setPosition(pX, pY);
    }

    private void toast(final String pMessage) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PongGameActivity.this, pMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class PongServerConnector extends ServerConnector<SocketConnection> implements PongConstants {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        // ===========================================================
        // Constructors
        // ===========================================================

        public PongServerConnector(final String pServerIP, final SocketConnectionServerConnector.ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
            super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    PongGameActivity.this.finish();
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    Debug.d("CLIENT: Connection established.");
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH, ConnectionRejectedProtocolMissmatchServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage) pServerMessage;
                    if (connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() > PROTOCOL_VERSION) {
                        //                                              Toast.makeText(context, text, duration).show();
                    } else if (connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() < PROTOCOL_VERSION) {
                        //                                              Toast.makeText(context, text, duration).show();
                    }
                    PongGameActivity.this.finish();
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) pServerMessage;
                    final long roundtripMilliseconds = System.currentTimeMillis() - connectionPongServerMessage.getTimestamp();
                    Debug.v("Ping: " + roundtripMilliseconds / 2 + "ms");
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_SET_PADDLEID, SetPaddleIDServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final SetPaddleIDServerMessage setPaddleIDServerMessage = (SetPaddleIDServerMessage) pServerMessage;
                    PongGameActivity.this.setPaddleID(setPaddleIDServerMessage.mPaddleID);
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage) pServerMessage;
                    PongGameActivity.this.updateScore(updateScoreServerMessage.mPaddleID, updateScoreServerMessage.mScore);
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL, UpdateBallServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage) pServerMessage;
                    PongGameActivity.this.updateBall(updateBallServerMessage.mX, updateBallServerMessage.mY);
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE, UpdatePaddleServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage) pServerMessage;
                    PongGameActivity.this.updatePaddle(updatePaddleServerMessage.mPaddleID, updatePaddleServerMessage.mX, updatePaddleServerMessage.mY);
                }
            });

        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
    }

    private class ExampleServerConnectorListener implements SocketConnectionServerConnector.ISocketConnectionServerConnectorListener {
        @Override
        public void onStarted(final ServerConnector<SocketConnection> pServerConnector) {
            PongGameActivity.this.toast("CLIENT: Connected to server.");
        }

        @Override
        public void onTerminated(final ServerConnector<SocketConnection> pServerConnector) {
            PongGameActivity.this.toast("CLIENT: Disconnected from Server.");
            PongGameActivity.this.finish();
        }
    }

    private class ExampleClientConnectorListener implements SocketConnectionClientConnector.ISocketConnectionClientConnectorListener {
        @Override
        public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
            PongGameActivity.this.toast("SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }

        @Override
        public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
            PongGameActivity.this.toast("SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }
    }
}
