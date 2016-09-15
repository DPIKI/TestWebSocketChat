package dis.testwebsocketchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        NetworkManager nm = new NetworkManager(this, null);
        nm.connect();
    }

    void initRecyclerView() {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(new ItemClickListener());
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public class ItemClickListener implements RecyclerAdapter.OnViewClickListener {
        @Override
        public void onViewClicked(RecyclerAdapterMessage m, int position) {
            if (m == null)
                return;
        }
    }


}
