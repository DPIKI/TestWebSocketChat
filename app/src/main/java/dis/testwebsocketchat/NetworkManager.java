package dis.testwebsocketchat;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Lenovo on 13.09.2016.
 */
public class NetworkManager {

    private Context mContext;

    private WebSocket mSocket;

    private EventBus mEventBus;

    private HandlerThread mHandlerThread;

    private Handler mHandler;

    private Handler mMainThreadHandler;

    public NetworkManager(Context context, EventBus eventBus) {
        this.mContext = context;
        this.mEventBus = eventBus;
        this.mHandlerThread = new HandlerThread("NetworkManagerThread");
        mHandlerThread.start();
        this.mHandler = new Handler(mHandlerThread.getLooper());
        this.mMainThreadHandler = new Handler();
    }

}
