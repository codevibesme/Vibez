package com.soham.vibez.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.soham.vibez.R;
import com.soham.vibez.model.Chat;

import java.util.ArrayList;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Chat> mChats;
    FirebaseUser mUser;
    final int RECEIVED = 0, SENT = 1;
    public MessageViewAdapter(Context context, ArrayList<Chat> mChats) {
        this.context = context;
        this.mChats = mChats;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.chat_item_right, parent, false);
        int dir=SENT;
        if(viewType == RECEIVED) {
            itemView = inflater.inflate(R.layout.chat_item_left, parent, false);
            dir = RECEIVED;
        }
        return new ViewHolder(itemView, dir);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        holder.chatTv.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChats!=null? mChats.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = mChats.get(position);
        if(chat.getReceiver_id().equals(mUser.getUid())){
            return RECEIVED;
        }
        return SENT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView chatTv;
        public ViewHolder(@NonNull View itemView, int dir) {
            super(itemView);
            if(dir==RECEIVED){
                chatTv = itemView.findViewById(R.id.chat_received);
            }
            else chatTv = itemView.findViewById(R.id.chat_sent);
        }
    }
}
