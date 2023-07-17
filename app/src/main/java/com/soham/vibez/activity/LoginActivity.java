package com.soham.vibez.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.soham.vibez.R;
import com.soham.vibez.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    LoginClickHandler clickHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        clickHandler = new LoginClickHandler(this);
        loginBinding.setHandler(clickHandler);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user!=null){
            Toast.makeText(this, "Exisitng user!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, UserActivity.class);
            startActivity(i);
        }
    }

    public class LoginClickHandler{
        Context context;

        public LoginClickHandler(Context context) {
            this.context = context;
        }
        public void onLogin(View view){
            String email = loginBinding.etEmail.getText().toString().trim();
            String password = loginBinding.etPassword.getText().toString().trim();
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, UserActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(context, "Failed to Logged in!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to Logged in!: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else Toast.makeText(context, "Fields are empty!", Toast.LENGTH_SHORT).show();
        }
        public void onNewUser(View view){
            Toast.makeText(context, "Sign up new User!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, RegisterActivity.class);
            startActivity(i);
        }
    }
}