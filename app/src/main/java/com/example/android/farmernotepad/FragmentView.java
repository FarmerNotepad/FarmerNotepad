package com.example.android.farmernotepad;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class FragmentView extends Fragment {
    MainActivity myActivity;
    ConstraintLayout list, grid, staggered;

    public FragmentView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        myActivity = (MainActivity) getActivity();
        list = view.findViewById(R.id.listLayout);
        grid = view.findViewById(R.id.gridLayout);
        staggered = view.findViewById(R.id.staggeredLayout);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("view_type", 0);
                editor.apply();
                myActivity.viewTypeHandler(0);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("view_type", 1);
                editor.apply();
                myActivity.viewTypeHandler(1);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        staggered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("view_type", 2);
                editor.apply();
                myActivity.viewTypeHandler(2);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        return view;
    }
}
