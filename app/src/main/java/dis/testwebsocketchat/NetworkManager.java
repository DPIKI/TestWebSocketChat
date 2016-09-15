package dis.testwebsocketchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Lenovo on 13.09.2016.
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private Context mContext;

    private EventBus mEventBus;

    private WebSocket mSocket;

    private ConnectionNotifierService mService;

    public NetworkManager(Context context, EventBus eventBus) {
        this.mContext = context;
        this.mEventBus = eventBus;
        Intent intent = new Intent(mContext, ConnectionNotifierService.class);
        mContext.bindService(intent, new ServiceConnection() {
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
        }, 0);
    }

    public void connect() {
        if (mSocket != null) {
            return;
        }

        try {
            mSocket = new WebSocketFactory()
                    .setConnectionTimeout(3000)
                    .createSocket("ws://127.0.0.1:2016")
                    .addListener(new SocketListener())
                    .connectAsynchronously();

            if (mService != null) {
                mService.showNotification();
            }

        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).postDelayed(this::connect, 1000);
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

    public boolean sendMessage(String username, String message) {
        // TODO: если это условие true то сообщение точно доставлено, а если нет, то может быть доставлено а может нет
        return isConnected() && mSocket.sendText(message).isOpen();
    }

    private void reconnect() {
        disconnect();
        connect();
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String text) {
            Log.d(TAG, "onTextMessage: " + text);
            Toast.makeText(mContext, "text", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected(WebSocket websocket,
                                   WebSocketFrame serverCloseFrame,
                                   WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) {
            reconnect();
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            reconnect();
        }
    }
}
