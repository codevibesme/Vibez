package com.soham.vibez.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soham.vibez.R;
import com.soham.vibez.adapter.MessageViewAdapter;
import com.soham.vibez.databinding.ActivityMessageBinding;
import com.soham.vibez.model.Chat;
import com.soham.vibez.model.ChatList;
import com.soham.vibez.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {
    ActivityMessageBinding messageBinding;
    MessageClickHandler messageClickHandler;
    String receiver_id, sender_id;
    String receiverName;
    DatabaseReference msgRef;
    DatabaseReference chatListRef;
    FirebaseUser mUser;

    // Data source for adapter
    ArrayList<Chat> mChats;
    // Recyclerview
    RecyclerView recyclerView;
    // Adapter
    MessageViewAdapter adapter;
    ChatList chatList;
    String senderName;
    String receiver_dp;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        messageClickHandler = new MessageClickHandler(this);
        messageBinding.setClickHandler(messageClickHandler);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("User");
        SharedPreferences userSh = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        senderName = userSh.getString(mUser.getUid(), "");
        Intent i = getIntent();
        receiver_dp = i.getStringExtra("dp");
        receiverName = i.getStringExtra("username");
        receiver_id = i.getStringExtra("uid");
        messageBinding.profPicMsg.setImageResource(R.drawable.prof_pic);
        messageBinding.userNameMsg.setText(receiverName);
        msgRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatListRef = FirebaseDatabase.getInstance().getReference("ChatList");
        assert mUser!=null;
        sender_id = mUser.getUid();
        mChats = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewMsg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        viewChats();
    }

    public void viewChats(){
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Chat chat = snap.getValue(Chat.class);
                    assert chat!=null;
                    if((chat.getReceiver_id().equals(receiver_id) && chat.getSender_id().equals(sender_id)) ||
                    chat.getReceiver_id().equals(sender_id) && chat.getSender_id().equals(receiver_id)){
                        mChats.add(chat);
//                        updateChatList(chat.getMessage());
                        chatList = new ChatList();

                        chatList.setLastMessage(chat.getMessage());
                        chatList.setUser(senderName);
                        chatList.setId(sender_id);
                        chatList.setDp("");
                        chatListRef.child(receiverName)
                                .child(senderName).setValue(chatList);

                        chatList.setUser(receiverName);
                        chatList.setId(receiver_id);
                        chatList.setDp(receiver_dp);
                        chatListRef.child(senderName)
                                .child(receiverName).setValue(chatList);
                    }
                }
                adapter = new MessageViewAdapter(getApplicationContext(), mChats);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateChatList(String message){
        chatListRef.child(sender_id).child(receiver_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRef.child(receiver_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        User user = snapshot.getValue(User.class);
                        chatListRef.child(sender_id).child(receiver_id).child("lastMessage").setValue(message);
                        for(DataSnapshot snap: snapshot1.getChildren()){
                            String key = snap.getKey();
                            assert key!=null;
                            Object value = snap.getValue();
                            assert value!=null;
                            if(key.equals("name")){
                                chatListRef.child(sender_id).child(receiver_id).child("user").setValue(user.getName());
                            }
                            else if(key.equals("imageUrl")){
                                chatListRef.child(sender_id).child(receiver_id).child("dp").setValue(user.getImageUrl());
                            }
                            else if(key.equals("uid")){
                                chatListRef.child(sender_id).child(receiver_id).child("id").setValue(user.getUid());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatListRef.child(receiver_id).child(sender_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRef.child(sender_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        User user = snapshot.getValue(User.class);
                        chatListRef.child(receiver_id).child(sender_id).child("lastMessage").setValue(message);
                        for(DataSnapshot snap: snapshot1.getChildren()){
                            String key = snap.getKey();
                            assert key!=null;
                            Object value = snap.getValue();
                            assert value!=null;
                            Toast.makeText(MessageActivity.this, "h->"+snap.getKey(), Toast.LENGTH_SHORT).show();
                            if(key.equals("name")){
                                chatListRef.child(receiver_id).child(sender_id).child("user").setValue(user.getName());
                            }
                            else if(key.equals("imageUrl")){
                                chatListRef.child(receiver_id).child(sender_id).child("dp").setValue(user.getImageUrl());
                            }
                            else if(key.equals("uid")){
                                chatListRef.child(receiver_id).child(sender_id).child("id").setValue(user.getUid());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class MessageClickHandler{
        Context context;

        public MessageClickHandler(Context context) {
            this.context = context;
        }

        public void onSend(View view){
            String message = messageBinding.textSpace.getText().toString().trim();
            if(!message.equals("")){
                HashMap<String, String> chat_map = new HashMap<>();
                chat_map.put("message", message);
                chat_map.put("receiver_id", receiver_id);
                chat_map.put("sender_id", sender_id);
                msgRef.push().setValue(chat_map);
                messageBinding.textSpace.setText("");
                viewChats();
                Toast.makeText(context, "Sent!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}