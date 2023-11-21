package com.daniel.plusnote.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.daniel.plusnote.activities.WeekPager;

public class WeekAdapter extends FragmentStateAdapter {
    public WeekAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(WeekPager.newInstance(position));
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
