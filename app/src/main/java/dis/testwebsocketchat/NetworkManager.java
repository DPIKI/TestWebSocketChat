package dis.testwebsocketchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;

import java.net.URISyntaxException;

/**
 * Created by Lenovo on 13.09.2016.
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private EventBus mEventBus;

    private Socket mSocket;

    private ConnectionNotifierService mService;

    private Handler mHandler;

    private Context mContext;

    public NetworkManager(Context context, EventBus eventBus) {
        this.mContext = context;
        this.mEventBus = eventBus;
        this.mHandler = new Handler(Looper.getMainLooper());
        context.bindService(
                new Intent(context, ConnectionNotifierService.class),
                new NmServiceConnection(), 0);
        IO.Options opts = new IO.Options();
        opts.transports = new String[]{"websocket"};
        try {
            mSocket = IO.socket("http://192.168.137.1:3000", opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {

        mSocket.on(Socket.EVENT_CONNECT, (objects) -> mHandler.post(() -> onEventConnect(objects)));
        mSocket.on(Socket.EVENT_CONNECT_ERROR, (objects) -> mHandler.post(() -> onEventConnectError(objects)));
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, (objects) -> mHandler.post(() -> onEventConnectTimeout(objects)));
        mSocket.on(Socket.EVENT_DISCONNECT, (objects) -> mHandler.post(() -> onEventDisconnect(objects)));
        mSocket.on(Socket.EVENT_ERROR, (objects) -> mHandler.post(() -> onEventError(objects)));
        mSocket.on(Socket.EVENT_MESSAGE, (objects) -> mHandler.post(() -> onEventMessage(objects)));
        mSocket.on(Socket.EVENT_RECONNECT, (objects) -> mHandler.post(() -> onEventReconnect(objects)));
        mSocket.on(Socket.EVENT_RECONNECT_ATTEMPT, (objects) -> mHandler.post(() -> onEventReconnectAttempt(objects)));
        mSocket.on(Socket.EVENT_RECONNECT_ERROR, (objects) -> mHandler.post(() -> onEventReconnectError(objects)));
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, (objects) -> mHandler.post(() -> onEventReconnectFailed(objects)));
        mSocket.on(Socket.EVENT_RECONNECTING, (objects) -> mHandler.post(() -> onEventReconnecting(objects)));
        mSocket.connect();

    }

    public void disconnect() {
        mSocket.off();
        mSocket.disconnect();
    }

    public boolean isConnected() {
        return false;
    }

    public void sendMessage(String username, String message) {
        mSocket.send(username + " : " + message);
    }

    private void onEventConnect(Object... objects) {
        Log.d(TAG, "Connect");
    }

    private void onEventConnectError(Object... objects) {
        Log.d(TAG, "ConnectError");
    }

    private void onEventConnectTimeout(Object... objects) {
        Log.d(TAG, "ConnectTimeout");
    }

    private void onEventDisconnect(Object... objects) {
        Log.d(TAG, "Disconnect");
    }

    private void onEventError(Object... objects) {
        Log.d(TAG, "Error");
    }

    private void onEventMessage(Object... objects) {
        Log.d(TAG, "Message : " + objects[0].toString());

        NewMessageEvent nme = new NewMessageEvent("", objects[0].toString(), "");
        mEventBus.post(nme);
    }

    private void onEventReconnect(Object... objects) {
        Log.d(TAG, "Reconnect");
    }

    private void onEventReconnectAttempt(Object... objects) {
        Log.d(TAG, "ReconnectAttempt");
    }

    private void onEventReconnectError(Object... objects) {
        Log.d(TAG, "ReconnectError");
    }

    private void onEventReconnectFailed(Object... objects) {
        Log.d(TAG, "ReconnectFailed");
    }

    private void onEventReconnecting(Object... objects) {
        Log.d(TAG, "Reconnecting");
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
