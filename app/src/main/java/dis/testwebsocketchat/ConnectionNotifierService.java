package dis.testwebsocketchat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ConnectionNotifierService extends Service {
    public ConnectionNotifierService() {
    }

    public void showNotification() {

    }

    public void hideNotification() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        ConnectionNotifierService service() {
            return ConnectionNotifierService.this;
        }
    }
}
