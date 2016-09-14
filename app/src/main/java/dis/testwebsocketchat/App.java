package dis.testwebsocketchat;

import android.app.Application;

/**
 * Created by Lenovo on 14.09.2016.
 */
public class App extends Application {
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .build();
    }

    public static AppComponent app() {
        return mAppComponent;
    }
}
