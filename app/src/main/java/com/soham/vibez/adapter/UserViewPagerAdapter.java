package com.soham.vibez.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.soham.vibez.fragment.ChatsFragment;
import com.soham.vibez.fragment.UsersFragment;

public class UserViewPagerAdapter extends FragmentStateAdapter {

    public UserViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new UsersFragment();
        }
        return new ChatsFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }

}
