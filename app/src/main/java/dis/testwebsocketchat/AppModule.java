package dis.testwebsocketchat;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lenovo on 14.09.2016.
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    public Context context() {
        return mContext;
    }

    @Provides
    @Singleton
    public NetworkManager networkManager(Context context, EventBus eventBus) {
        NetworkManager networkManager = new NetworkManager(context, eventBus);
        //networkManager.connect();
        return networkManager;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public PrefManager prefManager(Context context) {
        return new PrefManager(context);
    }
}
