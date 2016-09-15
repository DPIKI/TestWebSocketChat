package dis.testwebsocketchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 13.09.2016.
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private EventBus mEventBus;

    private WebSocket mSocket;

    private ConnectionNotifierService mService;

    private Handler mHandler;

    public NetworkManager(Context context, EventBus eventBus) {
        this.mEventBus = eventBus;
        this.mHandler = new Handler(Looper.getMainLooper());
        context.bindService(
                new Intent(context, ConnectionNotifierService.class),
                new NmServiceConnection(), 0);
    }

    public void connect() {
        if (mSocket != null) {
            return;
        }

        try {
            mSocket = new WebSocketFactory()
                    .setConnectionTimeout(3000)
                    .createSocket("ws://192.168.1.109:2016")
                    .addListener(new SocketListener())
                    .connectAsynchronously();
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).postDelayed(this::connect, 1000);
        }

        if (mService != null) {
            mService.showNotification();
        }

    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.clearListeners();
            mSocket.disconnect();
            mSocket = null;
        }

        if (mService != null) {
            mService.hideNotification();
        }
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.isOpen();
    }

    public void sendMessage(String username, String message) {

        if (mSocket == null) {
            Log.d(TAG, "Socket null");
            return;
        } else {
            Log.d(TAG, "Socket not null");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            JSONObject body = new JSONObject();
            body.put("user", username);
            body.put("message", message);
            body.put("time", sdf.format(new Date()));

            JSONObject root = new JSONObject();
            root.put("type", "send");
            root.put("body", body);

            if (mSocket == null) {
                Log.d(TAG, "Socket null");
            } else {
                Log.d(TAG, "Socket not null");
            }

            mSocket.sendText(root.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reconnect() {
        disconnect();
        connect();
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String text) {
            mHandler.post(() -> {
                try {
                    JSONObject o = new JSONObject(text);

                    if (!o.has("type")) {
                        return;
                    }

                    String type = o.getString("type");
                    JSONObject body = o.getJSONObject("body");

                    if (type.equals("new") || type.equals("received")) {
                        NewMessageEvent event = new NewMessageEvent(
                                body.getString("user"),
                                body.getString("message"),
                                body.getString("time")
                        );
                        mEventBus.post(event);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            mHandler.post(NetworkManager.this::reconnect);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            mHandler.post(NetworkManager.this::reconnect);
        }
    }

    private class NmServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((ConnectionNotifierService.Binder) iBinder).service();

            if (mSocket != null) {
                mService.showNotification();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    }
}
