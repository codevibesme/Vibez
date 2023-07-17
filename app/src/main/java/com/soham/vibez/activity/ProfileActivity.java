package com.soham.vibez.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soham.vibez.R;
import com.soham.vibez.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sh;
    FirebaseUser mUser;
    FirebaseDatabase db;
    DatabaseReference userRef;
    String username;
    String uid;
    String photoUrl;
    EditText et;
    ImageView dp;
    Button btn;
    int SELECT_PICTURE = 1;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        sh = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        username = sh.getString(mUser.getUid(), "");
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("Users");
        et = findViewById(R.id.userNameEt);
        et.setText(username);
        uid = mUser.getUid();
        dp = findViewById(R.id.prof_picProf);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageChooser();
            }

        });
        btn = findViewById(R.id.updateBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et.getText().toString().trim();
                sh.edit().putString(uid,username).apply();
                photoUrl = String.valueOf(selectedImageUri);
                updateUser();
            }
        });
    }
    public void updateUser(){
        Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        Map<String, Object> userData = new HashMap<String, Object>();
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    assert snap.getKey()!=null;
                    if(snap.getKey().equals("name"))
                        userRef.child(uid).child(snap.getKey()).setValue(username);
                    else if(snap.getKey().equals("imageUrl"))
                        userRef.child(uid).child(snap.getKey()).setValue(photoUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getImageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            if(requestCode == SELECT_PICTURE){

                selectedImageUri = null;
                if(data!=null)
                    selectedImageUri = data.getData();
                if(selectedImageUri!=null){
                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.prof_pic)
                            .into(dp);
                }
            }
        }

    }
}