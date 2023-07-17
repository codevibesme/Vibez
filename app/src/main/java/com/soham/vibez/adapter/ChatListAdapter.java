package com.soham.vibez.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soham.vibez.R;
import com.soham.vibez.activity.MessageActivity;
import com.soham.vibez.model.ChatList;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatList> chatList;

    public ChatListAdapter(Context context, ArrayList<ChatList> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.inbox_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        ChatList chatItem = chatList.get(position);

        holder.user_name.setText(chatItem.getUser());
        holder.lastMsg.setText(chatItem.getLastMessage());
        holder.user_dp.setImageResource(R.drawable.prof_pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("username", chatItem.getUser());
                i.putExtra("dp",  chatItem.getDp());
                i.putExtra("uid",  chatItem.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList!=null? chatList.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_dp;
        TextView user_name, lastMsg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_dp = itemView.findViewById(R.id.prof_pic_inbox);
            user_name = itemView.findViewById(R.id.user_name_inbox);
            lastMsg = itemView.findViewById(R.id.lastMsg);
        }
    }
}
