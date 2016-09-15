package dis.testwebsocketchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    EventBus eventBus;

    @Inject
    NetworkManager networkManager;

    @Inject
    PrefManager prefManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        setSupportActionBar(toolbar);

        mAdapter = new RecyclerAdapter();
        RecyclerView recyclerView = new RecyclerView(this);
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
                .setPositiveButton("OK", (d, b) -> {});
    }

    private void send() {
        networkManager.sendMessage(prefManager.getUserName(), editMessage.getText().toString());
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
