package com.soham.vibez.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soham.vibez.R;
import com.soham.vibez.activity.MessageActivity;
import com.soham.vibez.model.User;

import java.util.ArrayList;

public class RecyclerViewAdapterUsers extends RecyclerView.Adapter<RecyclerViewAdapterUsers.ViewHolder> {

    private ArrayList<User> userList;
    Context context;

    public RecyclerViewAdapterUsers(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_user_list, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String username = userList.get(position).getName();
        String user_prof_pic = userList.get(position).getImageUrl();
        String user_id = userList.get(position).getUid();
        if(user_prof_pic.equals(""))
            holder.user_dp.setImageResource(R.drawable.prof_pic);
        holder.user_name.setText(username);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("username", username);
                i.putExtra("dp", user_prof_pic);
                i.putExtra("uid", user_id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList==null?0:userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_dp;
        TextView user_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_dp = (ImageView) itemView.findViewById(R.id.prof_pic);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }
}
