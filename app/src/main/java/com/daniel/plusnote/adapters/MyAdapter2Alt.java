package com.daniel.plusnote.adapters;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.daniel.plusnote.activities.PagerFragment2Alt;

@SuppressLint("NotifyDataSetChanged")
public class MyAdapter2Alt extends FragmentStateAdapter {
    public MyAdapter2Alt(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (PagerFragment2Alt.newInstance(position));
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
