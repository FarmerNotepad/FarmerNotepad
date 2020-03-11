package com.example.android.farmernotepad;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class FragmentView extends Fragment {

    ImageButton list, grid, staggered;

    public FragmentView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        list = view.findViewById(R.id.listBtn);
        grid = view.findViewById(R.id.gridBtn);
        staggered = view.findViewById(R.id.staggeredBtn);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = MainActivity.getRecyclerView();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });


        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = MainActivity.getRecyclerView();
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }
        });


        staggered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = MainActivity.getRecyclerView();
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            }
        });


        return view;
    }
}
