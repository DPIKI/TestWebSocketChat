package dis.testwebsocketchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public List<RecyclerAdapterMessage> mData;
    private OnViewClickListener mItemClickListener;

    public RecyclerAdapter (OnViewClickListener listener) {
        mItemClickListener = listener;
    }

    public void update(List<RecyclerAdapterMessage> messages) {
        mData = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_chat, parent, false);

        return new ViewHolder(v, mItemClickListener);
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
        if (mData!= null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public LinearLayout llGeneral;
        public TextView tvSenderName;
        public TextView tvMessageText;
        public TextView tvTime;
        public RecyclerAdapterMessage message;

        OnViewClickListener listener;

        public ViewHolder(View v, OnViewClickListener listener) {
            super(v);
            this.listener = listener;
            message = null;
            tvSenderName = (TextView) v.findViewById(R.id.recycler_item_tv_sender_name);
            tvMessageText = (TextView) v.findViewById(R.id.recycler_item_tv_message_text);
            tvTime = (TextView) v.findViewById(R.id.recycler_item_tv_time);
            llGeneral = (LinearLayout) v.findViewById(R.id.recycler_item_rl_general);
            llGeneral.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.listener.onViewClicked(message, getAdapterPosition());
        }
    }

    public interface OnViewClickListener {
        void onViewClicked(RecyclerAdapterMessage message, int position);
    }

}

