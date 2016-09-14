package dis.testwebsocketchat;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lenovo on 14.09.2016.
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
