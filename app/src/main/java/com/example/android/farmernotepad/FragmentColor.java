package com.example.android.farmernotepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;


public class FragmentColor extends Fragment {

    private ImageButton allColors, redBtn, orangeBtn, yellowBtn, greenBtn, blueBtn, purpleBtn, blackBtn, greyBtn, whiteBtn;


    public FragmentColor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color, container, false);

        allColors = view.findViewById(R.id.allColorsBtn);
        redBtn = view.findViewById(R.id.redBtn);
        orangeBtn = view.findViewById(R.id.orangeBtn);
        yellowBtn = view.findViewById(R.id.yellowBtn);
        greenBtn = view.findViewById(R.id.greenBtn);
        blueBtn = view.findViewById(R.id.blueBtn);
        purpleBtn = view.findViewById(R.id.purpleBtn);
        blackBtn = view.findViewById(R.id.blackBtn);
        greyBtn = view.findViewById(R.id.greyBtn);
        whiteBtn = view.findViewById(R.id.whiteBtn);

        setClickListener(allColors, 0);
        setClickListener(redBtn, 1);
        setClickListener(orangeBtn, 2);
        setClickListener(yellowBtn, 3);
        setClickListener(greenBtn, 4);
        setClickListener(blueBtn, 5);
        setClickListener(purpleBtn, 6);
        setClickListener(blackBtn, 7);
        setClickListener(greyBtn, 8);
        setClickListener(whiteBtn, 9);


        return view;
    }

    private void setClickListener(ImageButton colorBtn, int colorNumber) {
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenericUtils.toast(getContext(), "Clicked");
            }
        });
    }
}
