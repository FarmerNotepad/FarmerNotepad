package com.example.android.farmernotepad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class FragmentSort extends Fragment {

    ConstraintLayout alphabetical, createDate, modDate, byColor;
    boolean desc = false;
    MainActivity myActivity;

    public FragmentSort() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        myActivity = (MainActivity) getActivity();
        alphabetical = view.findViewById(R.id.alphabeticalLayout);
        createDate = view.findViewById(R.id.createLayout);
        modDate = view.findViewById(R.id.modLayout);
        byColor = view.findViewById(R.id.colorLayout);

        alphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 1);
                editor.apply();
                myActivity.sortHandler(1);
            }
        });


        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 0);
                editor.apply();
                myActivity.sortHandler(0);
            }
        });


        modDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 2);
                editor.apply();
                myActivity.sortHandler(2);
            }
        });


        byColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 3);
                editor.apply();
                myActivity.sortHandler(3);
            }
        });

        return view;
    }
}
