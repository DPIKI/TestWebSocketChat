package dis.testwebsocketchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public List<RecyclerAdapterMessage> mData;

    public RecyclerAdapter() {
        this.mData = new ArrayList<>();
    }

    public void addMessage(RecyclerAdapterMessage message) {
        mData.add(message);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData != null) {
            RecyclerAdapterMessage m = mData.get(position);
            holder.tvSenderName.setText(m.senderName);
            holder.tvMessageText.setText(m.message);
            holder.tvTime.setText(m.time);
            holder.message = m;
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSenderName;
        public TextView tvMessageText;
        public TextView tvTime;
        public RecyclerAdapterMessage message;

        public ViewHolder(View v) {
            super(v);
            message = null;
            tvSenderName = (TextView) v.findViewById(R.id.recycler_item_tv_sender_name);
            tvMessageText = (TextView) v.findViewById(R.id.recycler_item_tv_message_text);
            tvTime = (TextView) v.findViewById(R.id.recycler_item_tv_time);
        }

    }

}

