package dis.testwebsocketchat;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    EventBus eventBus;

    @Inject
    NetworkManager networkManager;

    @BindView(R.id.checkbox)
    CheckBox checkBox;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.app().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        updateView();
    }

    void updateView() {
        if (networkManager.sendMessage("Username", "Message")) {
            Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Bad", Toast.LENGTH_SHORT).show();
        }

        checkBox.setChecked(networkManager.isConnected());

        new Handler(Looper.getMainLooper()).postDelayed(this::updateView, 1000);
    }
}
