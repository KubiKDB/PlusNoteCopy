package com.daniel.plusnote.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.daniel.plusnote.R;

public class WeekPager extends Fragment {
    private static int pageNumber;
    public static boolean weekType;

    public static WeekPager newInstance(int page) {
        WeekPager fragment2 = new WeekPager();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment2.setArguments(args);
        return fragment2;
    }

    public WeekPager() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = null;
        if (pageNumber == 0) {
            result = inflater.inflate(R.layout.week_fragment1, container, false);
            weekType = false;
        }
        if (pageNumber == 1) {
            result = inflater.inflate(R.layout.week_fragment2, container, false);
            weekType = true;
        }
        return result;
    }
}