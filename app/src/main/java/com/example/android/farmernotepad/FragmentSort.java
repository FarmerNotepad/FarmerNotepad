package com.example.android.farmernotepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;


public class FragmentSort extends Fragment {

    ImageButton alphabetical, createDate, modDate, byColor;

    public FragmentSort() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_short, container, false);

        alphabetical = view.findViewById(R.id.alphabeticalBtn);
        createDate = view.findViewById(R.id.createBtn);
        modDate = view.findViewById(R.id.modBtn);
        byColor = view.findViewById(R.id.colorBtn);

        alphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        modDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        byColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
