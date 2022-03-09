package com.visitegypt.presentation.home.parent;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.visitegypt.presentation.home.child.activity.ActivitiyChild;
import com.visitegypt.presentation.home.child.discover.DiscoverChild;

public class HomeAdapter extends FragmentStateAdapter {
    public HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public HomeAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public HomeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d("TAG", "createFragment: Home fragment ");
                return new DiscoverChild();

            case 1:
                Log.d("TAG", "createFragment: Activities fragment ");
                return new ActivitiyChild();
            default:
                throw new RuntimeException("Not supported");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
