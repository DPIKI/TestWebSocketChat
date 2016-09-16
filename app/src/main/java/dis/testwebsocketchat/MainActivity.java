package dis.testwebsocketchat;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URISyntaxException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main activity";
    @Inject
    EventBus eventBus;

    @Inject
    NetworkManager networkManager;

    @Inject
    PrefManager prefManager;

    @BindView(R.id.activity_main_rv)
    RecyclerView recyclerView;

    @BindView(R.id.activity_main_btn_send)
    ImageButton btnSend;

    @BindView(R.id.activity_main_edit_message)
    EditText editMessage;

    @BindView(R.id.chat_username_text_view)
    TextView tvUserName;

    RecyclerAdapter mAdapter;

    AlertDialog editUserNameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.app().inject(this);

        ButterKnife.bind(this);

        eventBus.register(this);

        // TODO: не инициализируется через @BindView.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new RecyclerAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        tvUserName.setText(prefManager.getUserName());
        btnSend.setOnClickListener(view -> send());

        EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        new AlertDialog.Builder(this)
                .setTitle("Enter username")
                .setView(editText)
                .setPositiveButton("OK", (d, b) -> {
                });

        RecyclerAdapterMessage message = new RecyclerAdapterMessage("name", "Test message:ansjdknaskdjnajkndkjandkjnakjdnasndkjansdjkanskjdnsadj", "20:16");
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
        mAdapter.addMessage(message);
    }

    private void send() {
        networkManager.sendMessage("username", editMessage.getText().toString());
    }

    @Subscribe
    public void onNewMessageEvent(NewMessageEvent event) {
        if (event == null)
            return;

        RecyclerAdapterMessage message = new RecyclerAdapterMessage(
                event.user,
                event.message,
                event.time
        );
        mAdapter.addMessage(message);
    }

}
