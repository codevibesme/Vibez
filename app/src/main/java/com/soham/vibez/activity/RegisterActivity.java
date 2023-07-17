package com.soham.vibez.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soham.vibez.R;
import com.soham.vibez.databinding.ActivityRegisterBinding;
import com.soham.vibez.model.User;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding registerBinding;
    RegisterClickHandler clickHandler;

    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseDatabase db;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        clickHandler = new RegisterClickHandler(this);
        registerBinding.setHandler(clickHandler);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("Users");
        user = mAuth.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user!=null){
            Toast.makeText(this, "user logged in!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, UserActivity.class);
            startActivity(i);
        }

    }

    public class RegisterClickHandler{
        Context context;
        public RegisterClickHandler(Context context) {
            this.context = context;
        }
        public void onSignup(View view){
            createNewUser();
        }
        public void onExistingUser(View view){
            Toast.makeText(context, "Existing User!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, LoginActivity.class);
            startActivity(i);
        }
        void createNewUser(){
            String name = registerBinding.etName.getText().toString().trim();
            String email = registerBinding.etEmail.getText().toString().trim();
            String password = registerBinding.etPassword.getText().toString().trim();

            Toast.makeText(context, "Password: "+password, Toast.LENGTH_SHORT).show();
            String imageUrl = "";
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    user = mAuth.getCurrentUser();
                                    assert user!=null;
                                    // Storing receiverName globally
                                    SharedPreferences user_sh = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
                                    SharedPreferences.Editor userEditor = user_sh.edit();
                                    userEditor.putString(user.getUid(), name);
                                    userEditor.putString(user.getUid()+"pass", password);
                                    userEditor.apply();

                                    User user1 = new User(name,email, user.getUid(), password, imageUrl);
                                    userRef.child(user.getUid()).setValue(user1);
                                    Toast.makeText(context, "User Added!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, UserActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Toast.makeText(context, "Empty Fields Detected!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}