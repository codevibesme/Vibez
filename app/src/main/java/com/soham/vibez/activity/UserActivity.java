package com.soham.vibez.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soham.vibez.R;
import com.soham.vibez.adapter.UserViewPagerAdapter;
import com.soham.vibez.databinding.ActivityUserBinding;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {
    ActivityUserBinding userBinding;
    FirebaseDatabase db;
    DatabaseReference userRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        // Toolbar setup
        setSupportActionBar(userBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null){
            displayUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutMenu){
            mAuth.signOut();
            Toast.makeText(this, "User signed out!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.profile){
            Toast.makeText(this, "Profile!!!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayUI(){
        ViewPager2 viewPager = userBinding.viewPager;
        TabLayout tabLayout = userBinding.tabLayout;
        FragmentManager fragmentManager = getSupportFragmentManager();
        UserViewPagerAdapter adapter = new UserViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==1)
                    tab.setText("Users");
                else tab.setText("Chats");
            }
        }).attach();
    }
}