package com.example.android.farmernotepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class FragmentSort extends Fragment {

    ImageButton alphabetical, createDate, modDate, byColor;
    boolean desc = false;
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
                ArrayList<ListItem> allNotesList = MainActivity.getAllNotesList();
                RecyclerViewAdapterMain adapter = new MainActivity().getAdapter();
                allNotesList = GenericUtils.sortByTitle(allNotesList, desc);
                adapter.notifyDataSetChanged();
                desc = !desc;
            }
        });


        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListItem> allNotesList = MainActivity.getAllNotesList();
                RecyclerViewAdapterMain adapter = new MainActivity().getAdapter();
                allNotesList = GenericUtils.sortByCreateDate(allNotesList, desc);
                adapter.notifyDataSetChanged();
                desc = !desc;
            }
        });


        modDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListItem> allNotesList = MainActivity.getAllNotesList();
                RecyclerViewAdapterMain adapter = new MainActivity().getAdapter();
                allNotesList = GenericUtils.sortByModDate(allNotesList, desc);
                adapter.notifyDataSetChanged();
                desc = !desc;
            }
        });


        byColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListItem> allNotesList = MainActivity.getAllNotesList();
                RecyclerViewAdapterMain adapter = new MainActivity().getAdapter();
                allNotesList = GenericUtils.sortByColor(allNotesList, desc);
                adapter.notifyDataSetChanged();
                desc = !desc;
            }
        });

        return view;
    }
}
