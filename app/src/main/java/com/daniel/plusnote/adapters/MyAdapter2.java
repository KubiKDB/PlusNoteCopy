package com.daniel.plusnote.adapters;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.daniel.plusnote.activities.PagerFragment2;

@SuppressLint("NotifyDataSetChanged")
public class MyAdapter2 extends FragmentStateAdapter {
    public MyAdapter2(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (PagerFragment2.newInstance(position));
    }

    public void refreshFragment(int position) {
        createFragment(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 262;
    }
}
